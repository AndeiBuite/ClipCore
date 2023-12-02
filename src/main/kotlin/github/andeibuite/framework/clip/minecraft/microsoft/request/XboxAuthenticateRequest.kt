package github.andeibuite.framework.clip.minecraft.microsoft.request

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class XboxAuthenticateRequest
(
	@SerializedName("Properties")
	var properties: JsonObject,

	@SerializedName("RelyingParty")
	var relyingParty: String?,

	@SerializedName("TokenType")
	var tokenType: String = "JWT",
)
