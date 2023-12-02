package clip


fun main()
{
	// 隐藏 Desktop 类在 macos Dock 中的图标 ( 这会导致 Swing 的图标也消失 )
	System.setProperty("apple.awt.UIElement","true")


}
