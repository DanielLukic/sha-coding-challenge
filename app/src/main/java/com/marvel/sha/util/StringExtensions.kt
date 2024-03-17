package com.marvel.sha.util

internal fun String.onNotBlank(block: (String) -> Unit) = if (isNotBlank()) block(this) else Unit
internal fun String.sanitized() = trim().replace(" \\(([a-zA-Z])".toRegex(), "\n($1")
