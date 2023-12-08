package io.github.andeibuite.framework.clip.utils

// Sun Nov 26 - Andei~Buite ~ 1.1 ~ 迁移自旧项目,轻微改动


// 系统类型(JavaAPI获取的原名)
val systemOriginalName: String = System.getProperty("os.name")



// 系统类型(枚举化)
val systemName: SystemName = run {
	val original = systemOriginalName.replace(" ","").lowercase()
	when
	{
		original.contains("win")-> SystemName.Windows
		original.contains("mac")-> SystemName.OSX
		original.contains("lin")-> SystemName.Linux
		else -> SystemName.Other
	}
}



// 系统类型枚举
enum class SystemName
{
	OSX ,
	Windows ,
	Linux ,
	Android ,
	IOS ,
	Other
}
