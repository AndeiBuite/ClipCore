package io.github.andeibuite.framework.clip.minecraft

import com.google.gson.GsonBuilder
import io.github.andeibuite.framework.clip.metadata.ClipConfig
import java.io.Serializable

class Minecraft private constructor (

	// Minecraft 文件结构
	val struct: MinecraftStruct,

	// Minecraft 游戏账户
	val account: MinecraftAccount,

	// Minecraft 游戏版本
	val version: String,

) : Serializable
{
	companion object
	{
		// 创建默认参数 Minecraft 实例
		fun createDefault(): Minecraft
		{
			return Minecraft( MinecraftStruct.createStandard("./minecraft"), MinecraftAccount.createDefault(), ClipConfig.defaultGameVersion )
		}

		// 提供完整参数以创建 Minecraft 实例
		fun create( struct: MinecraftStruct, account: MinecraftAccount, version: String ): Minecraft
		{
			return Minecraft( struct, account, version )
		}

	}

	fun toJson(): String = GsonBuilder().create().toJson(this)

	override fun toString(): String = this.toJson()

}
