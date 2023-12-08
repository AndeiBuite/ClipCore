package io.github.andeibuite.framework.clip.utils

import java.util.concurrent.TimeUnit

// Sun Nov 26 ~ Andei~Buite ~ 1.0


// 获取当前系统时间戳 (毫秒级)
fun getCurrentTime() = System.currentTimeMillis()


// 将秒转换为毫秒
fun secondToMilli( seconds: Long ) = TimeUnit.SECONDS.toMillis( seconds )
