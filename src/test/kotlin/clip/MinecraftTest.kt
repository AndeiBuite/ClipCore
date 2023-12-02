package clip

import github.andeibuite.framework.clip.minecraft.Minecraft
import github.andeibuite.framework.clip.minecraft.MinecraftAccount
import github.andeibuite.framework.clip.minecraft.MinecraftStruct


fun main()
{
	std()
}


fun std()
{
	// 创建 Minecraft 实例需要的参数
	val struct = MinecraftStruct.createDefault()
	val account = MinecraftAccount.createMicrosoft()
	val version = "1.17.1"

	// 创建 Minecraft 实例
	val minecraft = Minecraft.create( struct, account, version )

	// 打印 Minecraft 实例的内容
	println( "minecraft = \n$minecraft" )
}
