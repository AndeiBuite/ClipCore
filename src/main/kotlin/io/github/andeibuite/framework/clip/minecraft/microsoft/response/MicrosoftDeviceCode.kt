package io.github.andeibuite.framework.clip.minecraft.microsoft.response

import com.google.gson.annotations.SerializedName

data class MicrosoftDeviceCode
(

	// 验证码
	@SerializedName("user_code")
	var userCode: String,

	@SerializedName("device_code")
	var deviceCode: String,

	// 验证服务器地址
	@SerializedName("verification_uri")
	var verificationUri: String,

	@SerializedName("expires_in")
	var expiresIn: Long = 0,

	var interval: Long = 0,

	var message: String? = null,

	val json_source_zmbl:String = "123",
)
