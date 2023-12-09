package io.github.andeibuite.framework.clip.metadata

object ClipConfig
{
	// Minecraft 游戏账户名字长度限制范围(仅用于离线账号的检查) TODO 最后要详细考虑这个范围多少最合适
	var accountIdLengthLegalRange = UIntRange(1u, 5u)

	// 默认的 Minecraft 游戏版本号 TODO 默认版本号这个设定需要重新考虑一下
	var defaultGameVersion = "1.17.1"

	// 默认的 Minecraft 游戏账户名称 TODO 默认名称这个设定需要重新考虑一下
	var defaultAccountID = "SuperSteve"

	// 微软API所需的 client_id TODO 这个是 JMCCC 的, 记得自己搞一个
	var microsoftClientID = "d51b460a-0b8a-4696-af4d-690f7ba7f5b6"
}
