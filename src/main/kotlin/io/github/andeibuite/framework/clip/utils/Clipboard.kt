package io.github.andeibuite.framework.clip.utils

import java.awt.Toolkit
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection


// Sun Nov 26 ~ Andei.Buite ~ 1.0


object ClipboardUtils
{
	private val clipboard = Toolkit.getDefaultToolkit().systemClipboard

	// 将字符串写入剪切板
	fun setString( content: String, owner: ClipboardOwner? = null )
	{
		clipboard.setContents( StringSelection( content ), owner)
	}


	// 读取剪切板中内容
	fun getString(): String
	{
		val originalContent = clipboard.getContents(null)
		var content = ""
		if ( originalContent.isDataFlavorSupported(DataFlavor.stringFlavor) )
		{
			content = originalContent.getTransferData (DataFlavor.stringFlavor) as String
		}
		return content
	}

}
