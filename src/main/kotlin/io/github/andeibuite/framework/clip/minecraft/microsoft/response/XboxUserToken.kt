package io.github.andeibuite.framework.clip.minecraft.microsoft.response

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class XboxUserToken
(
	@SerializedName("IssueInstant")
	var issueInstant: String?,

	@SerializedName("NotAfter")
	var notAfter: String?,

	@SerializedName("Token")
	var token: String?,

	@SerializedName("DisplayClaims")
	var displayClaims: JsonObject = JsonObject(),

	@SerializedName("expires_in")
	var expiresIn: Int = 0,
)
