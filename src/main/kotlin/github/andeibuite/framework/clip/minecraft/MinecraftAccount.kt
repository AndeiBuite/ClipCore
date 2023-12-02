package github.andeibuite.framework.clip.minecraft

import github.andeibuite.framework.clip.extension.toJson
import github.andeibuite.framework.clip.metadata.ClipConfig
import github.andeibuite.framework.clip.utils.AccountUtils
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
		// 创建默认账户
		fun createDefault(): MinecraftAccount = createOffline( ClipConfig.defaultAccountID )

		// 创建离线账户
		fun createOffline( accountName: String ): MinecraftAccount
		{
			val infoPack = AccountUtils.generateInfoPack( accountName )
			return MinecraftAccount( accountName, infoPack.uuid, infoPack.token, "offline", null )
		}

		// 创建 Microsoft 账户
		fun createMicrosoft()
		{

		}

		// 创建 Yggdrasil 账户
		@Deprecated("Bad way to login", ReplaceWith("createMicrosoft()"))
		fun createYggdrasil()
		{
			TODO("Mojang 登录一点都没做")
		}

	}

	override fun toString(): String = this.toJson()

}
