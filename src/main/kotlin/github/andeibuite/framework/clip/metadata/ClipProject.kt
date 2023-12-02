package github.andeibuite.framework.clip.metadata

import github.andeibuite.framework.clip.utils.systemOriginalName

object ClipProject
{
	// 项目名称
	val name = "ClipCore"

	// 项目版本 TODO 发布正式版时记得与 pom.xml 中版本对齐
	val version = "1.1-SNAPSHOT"

	// 开发者列表
	val authorList = arrayListOf("andei.buite","zimoyin")

	// 小彩蛋ou~
	val easterEgg = "爱死墨墨了"


	// 向控制台打印项目详情
	fun printInfo()
	{
		println( "--- Minecraft Client Launcher Core ---" )
		println( "$name - $version" )
		println( "Developed By $authorList" )
		println( "Running at: $systemOriginalName" )
		println( "--------------------------------------" )

	}

}
