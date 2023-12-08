package io.github.andeibuite.framework.clip.utils

import io.github.andeibuite.framework.clip.metadata.ClipConfig
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*


object MinecraftAccountUtils
{
	// 生成 UUID 与 Token
	fun generateInfoPack( name: String ): InfoPack
	{
		val range = ClipConfig.accountIdLengthLegalRange
		val min = range.first.toInt()
		val max = range.last.toInt()
		if ( ( name.length > max ) and ( name.length < min ) )
			throw IllegalArgumentException("The name length is not illegal, Legal range: 1 ~ 2")
		val uuid = generateUuid( name )
		val token = getToken( uuid )
		return InfoPack(uuid, token)
	}


	// 字符串为种子生成 UUID
	private fun generateUuid( inputString: String ): UUID
	{

		lateinit var result : UUID
		try
		{
			// 使用命名空间为 UUIDv5 创建一个固定的 UUID
			val namespace = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8")

			// 将输入字符串转换为字节数组
			val inputBytes = inputString.toByteArray(StandardCharsets.UTF_8)

			// 计算输入字节数组的 SHA-1 哈希值
			val sha1 = MessageDigest.getInstance("SHA-1")
			val hashBytes = sha1.digest(inputBytes)

			// 设置版本为 5，并使用哈希值生成 UUID
			val mostSigBits = namespace.mostSignificantBits
			val leastSigBits = hashBytes[0].toLong() and 0xFFL shl 56 or
					(hashBytes[1].toLong() and 0xFFL shl 48) or
					(hashBytes[2].toLong() and 0xFFL shl 40) or
					(hashBytes[3].toLong() and 0xFFL shl 32) or
					(hashBytes[4].toLong() and 0xFFL shl 24) or
					(hashBytes[5].toLong() and 0xFFL shl 16) or
					(hashBytes[6].toLong() and 0xFFL shl 8) or
					(hashBytes[7].toLong() and 0xFFL)
			result = UUID(mostSigBits, leastSigBits)
		}
		catch (e: Exception)
		{
			throw e
		}
		return result
	}

	// UUID 去除非法字符得到 Token
	private fun getToken( uuid: UUID ): String
	{
		return uuid.toString().replace("-","")
	}

	// UUID 与 Token 信息包
	data class InfoPack( val uuid: UUID, val token: String )

}
