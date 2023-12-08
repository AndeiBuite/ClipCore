package io.github.andeibuite.framework.clip.minecraft

import io.github.andeibuite.framework.clip.extension.toJson
import java.io.File
import java.io.Serializable

class MinecraftStruct(

	val root: File,

	val versionsRoot: File,

	val assetsRoot: File,

	val librariesRoot: File,

	val clipModConfigRoot: File

): Serializable
{

	companion object
	{
		// 创建标准文件结构
		fun createStandard( minecraftStructRoot: File ): MinecraftStruct
		{
			return MinecraftStruct(
				minecraftStructRoot,
				File(minecraftStructRoot,"versions"),
				File(minecraftStructRoot, "assets"),
				File(minecraftStructRoot, "libraries"),
				File(minecraftStructRoot, "clipModConfig")
			)
		}

		fun createStandard( minecraftStructRoot: String ): MinecraftStruct = createStandard(File(minecraftStructRoot))


		// 创建版本隔离文件结构
		fun createIsolation()
		{

		}
	}

	override fun toString(): String = this.toJson()

}
