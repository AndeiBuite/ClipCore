package clip

import io.github.andeibuite.framework.clip.minecraft.Minecraft
import io.github.andeibuite.framework.clip.minecraft.MinecraftAccount
import io.github.andeibuite.framework.clip.minecraft.MinecraftStruct


fun main()
{
	std()
}


fun std()
{
	// 创建 Minecraft 实例需要的参数
	val struct = MinecraftStruct.createStandard("./minecraft")
	val account = MinecraftAccount.createOffline( "player" )
	val version = "1.17.1"

	// 创建 Minecraft 实例
	val minecraft = Minecraft.create( struct, account, version )

	// 打印 Minecraft 实例的内容
	println( "minecraft = \n${minecraft.toJson()}" )
}
