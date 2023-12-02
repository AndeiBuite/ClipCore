package github.andeibuite.framework.clip.minecraft.microsoft.response

import com.google.gson.annotations.SerializedName

data class MicrosoftToken
(
	@SerializedName("token_type")
	var tokenType: String? = null,

	@SerializedName("scope")
	var scope: String? = null,

	@SerializedName("expires_in")
	var expiresIn: Int = 0,

	@SerializedName("ext_expires_in")
	var extExpiresIn: Int = 0,

	@SerializedName("access_token")
	var accessToken: String? = null,

	@SerializedName("refresh_token")
	var refreshToken: String? = null,

	@SerializedName("error")
	var error: String? = null,
)
