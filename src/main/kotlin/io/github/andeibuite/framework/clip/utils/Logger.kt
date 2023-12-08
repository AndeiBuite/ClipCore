package io.github.andeibuite.framework.clip.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// Sun Nov 26 ~ zimoyin ~ 1.0 ~ 迁移自老项目


fun createLogger(name: String): Logger = LoggerFactory.getLogger(name)

fun createLogger(cls: Class<*>): Logger = LoggerFactory.getLogger(cls)

fun createLogger(obj: Any): Logger = LoggerFactory.getLogger(obj.javaClass)
