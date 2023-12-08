package io.github.andeibuite.framework.clip.utils

import io.github.andeibuite.framework.clip.utils.SystemName.Android
import io.github.andeibuite.framework.clip.utils.SystemName.IOS
import io.github.andeibuite.framework.clip.utils.SystemName.Linux
import io.github.andeibuite.framework.clip.utils.SystemName.OSX
import io.github.andeibuite.framework.clip.utils.SystemName.Other
import io.github.andeibuite.framework.clip.utils.SystemName.Windows
import java.awt.Desktop
import java.net.URI

// Sun Nov 26 ~ Andei~Buite ~ 1.1

object BrowserUtils
{

	private val logger = createLogger( this::class )

	// 浏览器异常
	class BrowserException( msg:String = "Unknown case" ): Exception( msg )

	// 网址格式正则表达式
	val webAddressRegex = Regex("^https?://[\\w.-]+\\.[a-zA-Z]{2,}[\\w.-]*(/[\\w.-]*)*/?$")


	// 打开指定地址 返回是否打开成功(不准确), url 需要满足 webAddressRegex 格式
	@Deprecated("我也不知道我咋想的能写出这个玩意")
	fun openUrl(url: String, osName: SystemName = systemName) : Boolean
	{
		if(!webAddressRegex.matches(url)) throw BrowserException("Format of URL is illegal to opening browser: $url")
		fun open(prefix:String) = try { Runtime.getRuntime().exec(arrayOf(prefix,url)); true } catch (e:Exception) { false }
		val pass  = "succeed to open url $url in browser" // 打开成功时日志内容
		val error = "failed to open url $url in browser"  // 打开失败时日志内容
		return when( osName )
		{
			OSX     ->  { logger.info(pass); open("open") }    // MacOS 使用「open」命令打开
			Windows ->  { logger.info(pass); open("start") }   // Windows 使用「start」命令打开
			Linux   ->  { logger.error(error); false }           // Linux 咋办我不到啊
			Android ->  { logger.error(error); false }           // Android 咋办我不到啊
			IOS     ->  { logger.error(error); false }           // IOS 咋办我不到啊
			Other   ->  { logger.error(error); false }           // 别的系统我更不知道了
		}
	}

	// 打开指定地址 返回是否打开成功(不准确)
	fun openUrl( uri: URI ): Boolean
	{
		val desktop = Desktop.getDesktop() ?: return false
		if ( !desktop.isSupported( Desktop.Action.BROWSE ) ) return false
		desktop.browse( uri )
		return true
	}
}
