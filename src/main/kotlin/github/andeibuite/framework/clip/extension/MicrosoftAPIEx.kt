package github.andeibuite.framework.clip.extension

import github.andeibuite.framework.clip.minecraft.microsoft.MicrosoftAPI
import github.andeibuite.framework.clip.minecraft.microsoft.response.MicrosoftSession
import github.andeibuite.framework.clip.minecraft.microsoft.response.MicrosoftToken
import github.andeibuite.framework.clip.minecraft.microsoft.response.XboxToken
import github.andeibuite.framework.clip.utils.createLogger
import github.andeibuite.framework.clip.utils.getCurrentTime


private val logger = createLogger( MicrosoftAPI::class.java )


// 函数字面意思
fun MicrosoftAPI.generateXboxToken( microsoftToken: MicrosoftToken): XboxToken
{
	logger.debug("Try to get xboxUserToken")
	val xboxUserToken = getXboxUserToken( microsoftToken )

	val xboxXstsToken = getXboxXstsToken( xboxUserToken, "http://xboxlive.com" )

	val xboxXstsMcToken = getXboxXstsToken( xboxUserToken, "rp://api.minecraftservices.com/" )

	val userHash = xboxXstsMcToken.displayClaims["xui"].asJsonArray[0].asJsonObject["uhs"].asString

	val minecraftToken = getMinecraftToken( userHash, xboxXstsMcToken )

	return XboxToken( xboxXstsToken.displayClaims["xui"]?.asJsonArray?.get(0)?.asJsonObject?.get("uhs")?.asString, minecraftToken.accessToken )
}


// 生成新的 MicrosoftSession
fun MicrosoftAPI.generateMicrosoftSession(): MicrosoftSession
{
	val microsoftToken = getMicrosoftToken ( getDeviceCode() )
	val xboxToken = MicrosoftAPI.generateXboxToken( microsoftToken )

	val microsoftAccessToken = microsoftToken.accessToken ?: throw IllegalArgumentException("Bad microsoftToken")
	val microsoftRefreshToken = microsoftToken.refreshToken ?: throw IllegalArgumentException("Bad microsoftToken")
	val minecraftAccessToken = xboxToken.minecraftAccessToken ?: throw IllegalArgumentException("Bad xboxToken or microsoftToken")
	val xboxUserID = xboxToken.xboxUserId ?: throw IllegalArgumentException("Bad xboxToken or microsoftToken")
	val createTimespan = getCurrentTime()
	val refreshTimespan = getCurrentTime() // TODO 这里一看就不对啊

	return MicrosoftSession(microsoftAccessToken, microsoftRefreshToken, minecraftAccessToken, xboxUserID, createTimespan, refreshTimespan)
}

// 解密 MicrosoftSession
fun MicrosoftAPI.decodeMicrosoftSession()
{

}


// 加密 MicrosoftSession
fun MicrosoftAPI.encodeMicrosoftSession()
{

}
