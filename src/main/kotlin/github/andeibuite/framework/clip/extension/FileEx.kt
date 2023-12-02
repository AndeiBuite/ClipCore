package github.andeibuite.framework.clip.extension

import java.io.File

// 获取文件下 N 级目录中文件
fun File.getChild( vararg name: String): File
{
	var result: File = this
	name.forEach { result = File( result, it ) }
	return result
}
