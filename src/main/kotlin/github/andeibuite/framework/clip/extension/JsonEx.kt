package github.andeibuite.framework.clip.extension

import com.google.gson.Gson


// 将对象转为 Json 字符串
fun Any.toJson(): String = Gson().toJson( this )
