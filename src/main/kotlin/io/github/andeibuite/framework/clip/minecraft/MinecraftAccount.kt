package io.github.andeibuite.framework.clip.minecraft

import io.github.andeibuite.framework.clip.extension.generateMicrosoftSession
import io.github.andeibuite.framework.clip.extension.toJson
import io.github.andeibuite.framework.clip.extension.toUUID
import io.github.andeibuite.framework.clip.metadata.ClipConfig
import io.github.andeibuite.framework.clip.minecraft.microsoft.MicrosoftAPI
import io.github.andeibuite.framework.clip.minecraft.microsoft.response.MicrosoftSession
import io.github.andeibuite.framework.clip.utils.MinecraftAccountUtils
import io.github.andeibuite.framework.clip.utils.createLogger
import java.io.Serializable
import java.util.UUID
import java.util.Date

class MinecraftAccount private constructor(

	// 账户名称, Minecraft 中的游戏ID
	val name: String,

	// 账户 UUID, Minecraft 中的唯一通行证
	val uuid: UUID,

	// 账户 Token, Minecraft 中的登录令牌
	val token: String,

	// 该账户类型, Minecraft 账号根据运营商不同分为多种类型
	val kind: String,

	// 账户超时日期, 为 NULL 代表永久不过期
	var maxAge: Date?,

): Serializable
{
	companion object
	{
		private val logger = createLogger( this::class )

		private class LoginException( msg: String = "unknown cause" ): Exception( msg )

		// 创建默认账户
		fun createDefault(): MinecraftAccount = createOffline( ClipConfig.defaultAccountID )

		// 创建离线账户
		fun createOffline( accountName: String ): MinecraftAccount
		{
			logger.debug("Try to login with offline")
			val infoPack = MinecraftAccountUtils.generateInfoPack( accountName )
			return MinecraftAccount( accountName, infoPack.uuid, infoPack.token, "offline", null )
		}

		// 创建新的 Microsoft 账户
		fun createMicrosoft( microsoftSession: MicrosoftSession = MicrosoftAPI.generateMicrosoftSession() ): MinecraftAccount
		{
			logger.debug("Try to login with microsoft")
			val profile = MicrosoftAPI.getMinecraftProfile( microsoftSession )

			val name = profile.name ?: throw LoginException("Bad profile")
			val uuidString = profile.id ?: throw LoginException("Bad profile")
			val token = microsoftSession.minecraftAccessToken

			logger.debug("Succeed to login with microsoft")
			return MinecraftAccount(name, uuidString.toUUID(), token, "microsoft", null)
		}

		// 创建 Yggdrasil 账户
		@Deprecated("Bad way to login",
			ReplaceWith("package github.andeibuite.framework.clip.minecraft.companion.createMicrosoft()"))
		fun createYggdrasil(): MinecraftAccount
		{
			logger.debug("Try to login with yggdrasil")
			TODO("Mojang 登录一点都没做")
		}

	}

	override fun toString(): String = this.toJson()

}
