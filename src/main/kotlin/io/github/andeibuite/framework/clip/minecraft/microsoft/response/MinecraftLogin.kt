package io.github.andeibuite.framework.clip.minecraft.microsoft.response

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class MinecraftLogin(

	@SerializedName("username")
	val username: String?,

	@SerializedName("roles")
	val roles: JsonArray,

	@SerializedName("metadata")
	val metadata: JsonObject,

	@SerializedName("access_token")
	val accessToken: String?,

	@SerializedName("token_type")
	val tokenType: String?

)
