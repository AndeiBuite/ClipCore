package github.andeibuite.framework.clip.extension

import java.util.*

fun String.toUUID(): UUID
{
	return when (this.length)
	{
		36 -> UUID.fromString(this)
		32 -> UUID.fromString(
			this.substring(0, 8) + "-" + this.substring(8, 12) + "-" + this.substring(12, 16) + "-" + this.substring(
				16,
				20
			) + "-" + this.substring(20, 32)
		)

		else -> throw IllegalArgumentException("Invalid UUID string: $this")
	}
}
