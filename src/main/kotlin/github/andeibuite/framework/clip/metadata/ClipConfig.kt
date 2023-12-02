package github.andeibuite.framework.clip.metadata

object ClipConfig
{
	// Minecraft 游戏账户名字长度限制范围
	var accountIdLengthLegalRange = UIntRange(1u, 2u)

	// 默认的 Minecraft 游戏版本号
	var defaultGameVersion = "1.17.1"

	// 默认的 Minecraft 游戏账户名称
	var defaultAccountID = ClipProject.name

	// 微软API所需的 client_id TODO 这个是 jmccc 的, 记得自己搞一个
	var microsoftClientID = "d51b460a-0b8a-4696-af4d-690f7ba7f5b6"
}
