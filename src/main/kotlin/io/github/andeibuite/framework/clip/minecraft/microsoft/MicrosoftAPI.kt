package io.github.andeibuite.framework.clip.minecraft.microsoft

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonIOException
import com.google.gson.JsonObject
import io.github.andeibuite.framework.clip.extension.toJson
import io.github.andeibuite.framework.clip.metadata.ClipConfig
import io.github.andeibuite.framework.clip.minecraft.microsoft.request.MinecraftLoginRequest
import io.github.andeibuite.framework.clip.minecraft.microsoft.request.XboxAuthenticateRequest
import io.github.andeibuite.framework.clip.minecraft.microsoft.response.MicrosoftDeviceCode
import io.github.andeibuite.framework.clip.minecraft.microsoft.response.MicrosoftSession
import io.github.andeibuite.framework.clip.minecraft.microsoft.response.MicrosoftToken
import io.github.andeibuite.framework.clip.minecraft.microsoft.response.MinecraftLogin
import io.github.andeibuite.framework.clip.minecraft.microsoft.response.MinecraftProfile
import io.github.andeibuite.framework.clip.minecraft.microsoft.response.XboxUserToken
import io.github.andeibuite.framework.clip.utils.BrowserUtils
import io.github.andeibuite.framework.clip.utils.ClipboardUtils
import io.github.andeibuite.framework.clip.utils.createLogger
import io.github.andeibuite.framework.clip.utils.getCurrentTime
import io.github.andeibuite.framework.clip.utils.secondToMilli
import org.apache.hc.client5.http.HttpResponseException
import org.apache.hc.client5.http.fluent.Form
import org.apache.hc.client5.http.fluent.Request
import org.apache.hc.core5.http.ClassicHttpResponse
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpException
import org.apache.hc.core5.http.HttpStatus
import org.apache.hc.core5.http.io.HttpClientResponseHandler
import org.apache.hc.core5.http.io.entity.EntityUtils
import java.io.IOException
import java.lang.reflect.Field
import java.net.URI

object MicrosoftAPI
{
	private val logger = createLogger( this::class )

	// 设备授权请求
	fun getDeviceCode(): MicrosoftDeviceCode
	{
		logger.debug("Try to get microsoftDeviceCode")
		val requestUrl = "https://login.microsoftonline.com/consumers/oauth2/v2.0/devicecode"
		logger.debug("POST:: $requestUrl")
		return Request.post( requestUrl ).bodyForm(
			Form.form()
				.add("client_id", ClipConfig.microsoftClientID)
				.add("scope","XboxLive.signin offline_access")
				.build()
		).execute().handleResponse(JsonResponseHandler(MicrosoftDeviceCode::class.java,true))
	}

	// 该函数可能会因 deviceCode 未生效而阻塞较长时间, 使用时请考虑所在线程是否允许较长时间阻塞
	// 获取 MicrosoftToken
	fun getMicrosoftToken( deviceCode: MicrosoftDeviceCode ): MicrosoftToken
	{
		logger.debug("Try to get microsoftToken")
		BrowserUtils.openUrl( URI(deviceCode.verificationUri) )
		ClipboardUtils.setString( deviceCode.userCode )
		logger.info("Provide code 「\u001B[31m${deviceCode.userCode}\u001B[0m」 which has already written to system clipboard to「${deviceCode.verificationUri}」")

		/* 获取结果 */
		val currentTime = getCurrentTime()                                  // 当前时间
		val interval = secondToMilli( deviceCode.interval )                 // 没搞明白到底为啥休息, 但是墨墨代码有这个
		val expireTime = currentTime + secondToMilli(deviceCode.expiresIn)  // 请求过期时间
		lateinit var result: MicrosoftToken                                 // 结果
		while ( currentTime < expireTime )
		{
			Thread.sleep(interval)  // 等待 deviceCode 生效, 可能会阻塞较长时间
			val requestUrl = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token"
			logger.debug("POST:: $requestUrl")
			result = Request.post(requestUrl).bodyForm(
				Form.form()
					.add("grant_type","urn:ietf:params:oauth:grant-type:device_code")
					.add("client_id",ClipConfig.microsoftClientID)
					.add("device_code",deviceCode.deviceCode)
					.build()
			).execute().handleResponse(JsonResponseHandler(MicrosoftToken::class.java,false))

			val key = when( result.error )
			{
				null -> true
				"authorization_pending" -> false
				else -> throw MicrosoftResponseException("Authentication failed: ${result.error}")
			}
			if ( key ) break
		}
		result.accessToken ?: throw MicrosoftResponseException("Authentication failed: Unknown cause")
		return result
	}

	fun getXboxUserToken( microsoftToken: MicrosoftToken ): XboxUserToken
	{
		val properties = JsonObject()
		properties.addProperty("AuthMethod", "RPS")
		properties.addProperty("SiteName","user.auth.xboxlive.com")
		properties.addProperty("RpsTicket","d=${microsoftToken.accessToken ?: throw MicrosoftResponseException("Bad microsoftToken")}")
		val args = XboxAuthenticateRequest( properties,"http://auth.xboxlive.com" )
		val gson = GsonBuilder().disableHtmlEscaping().create() // 必须 HTML 符号转义, 不然 '=' 就要变成 '\u003d' 喽
		val requestUrl = "https://user.auth.xboxlive.com/user/authenticate"
		logger.debug("POST:: $requestUrl")
		return Request.post( requestUrl )
			.bodyString( gson.toJson(args), ContentType.APPLICATION_JSON)
			.execute().handleResponse(JsonResponseHandler( XboxUserToken::class.java))
	}


	fun getXboxXstsToken( xboxUserToken: XboxUserToken, relyingParty: String ): XboxUserToken
	{
		logger.debug("Try to get xboxXstsToken")
		val properties = JsonObject()
		val userTokenArray = JsonArray()
		userTokenArray.add(xboxUserToken.token ?: throw JsonIOException("Bad xboxUserToken"))
		properties.addProperty("SandboxId","RETAIL")
		properties.add("UserTokens",userTokenArray)
		val args = XboxAuthenticateRequest( properties, relyingParty,"JWT" ).toJson()
		val requestUrl = "https://xsts.auth.xboxlive.com/xsts/authorize"
		logger.debug("POST:: $requestUrl")
		return Request.post("https://xsts.auth.xboxlive.com/xsts/authorize")
			.bodyString(args, ContentType.APPLICATION_JSON)
			.execute().handleResponse(JsonResponseHandler(XboxUserToken::class.java,true))
	}

	// 获取 Minecraft 通行令牌及附属信息
	fun getMinecraftToken( userHash: String, xboxXstsToken: XboxUserToken ): MinecraftLogin
	{
		logger.debug("Try to get minecraftToken")
		val requestUrl = "https://api.minecraftservices.com/authentication/login_with_xbox"
		logger.debug("POST:: $requestUrl")
		return Request.post( requestUrl )
			.bodyString(
				MinecraftLoginRequest(userHash, xboxXstsToken.token).toJson(),
				ContentType.APPLICATION_JSON
			)
			.execute().handleResponse(JsonResponseHandler(MinecraftLogin::class.java))
	}

	// 获取 Minecraft 账户信息
	fun getMinecraftProfile( session: MicrosoftSession): MinecraftProfile
	{
		logger.debug("Try to get minecraftProfile")
		val requestUrl = "https://api.minecraftservices.com/minecraft/profile"
		logger.debug("GET:: $requestUrl")
		return Request.get("https://api.minecraftservices.com/minecraft/profile")
			.setHeader("Authorization", String.format("Bearer %s", session.minecraftAccessToken ))
			.execute().handleResponse(JsonResponseHandler(MinecraftProfile::class.java))
	}


	/**
	 * @author zimoyin
	 */
	private class JsonResponseHandler<T> @JvmOverloads constructor(
		private val clazz: Class<T>,
		private val checkResponseCode: Boolean = true,
	) : HttpClientResponseHandler<T>
	{
		/**
		 * 如果bean 里面存在此字段，就给这个字段赋值json的源信息
		 */
		private val fieldName =  "zmbl_json_source"
		private val log = createLogger(this)

		@Throws(HttpException::class, IOException::class)
		override fun handleResponse(response: ClassicHttpResponse): T {
			val entity = response.entity
			var res: String? = null
			if (entity != null) {
				res = EntityUtils.toString(entity)
			}
			if (checkResponseCode && response.code >= HttpStatus.SC_REDIRECTION) {
				EntityUtils.consume(entity)
				throw HttpResponseException(response.code, response.reasonPhrase)
			}
			val obj: T = Gson().fromJson(res, clazz)

			runCatching {
				val clz = obj!!::class.java
				val fields = ArrayList<Field>()
				fields.addAll(clz.fields)
				fields.addAll(clz.declaredFields)

				for (field in fields) {
					if (field.name == fieldName && field.type.equals(String::class.java)) {
						field.isAccessible = true
						field.set(obj,res)
					}
				}
			}.onFailure {
				log.warn("Unable to assign ${obj?.let { it::class.java }} to this $fieldName through reflection")
			}

			return obj
		}
	}
}
