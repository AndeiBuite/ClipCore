package io.github.andeibuite.framework.clip.minecraft.microsoft.response



data class MicrosoftSession
(

	var microsoftAccessToken: String,

	var microsoftRefreshToken: String,

	var minecraftAccessToken: String,

	var xboxUserId: String,

	var createTimespan: Long,

	var refreshTimespan: Long,

)
