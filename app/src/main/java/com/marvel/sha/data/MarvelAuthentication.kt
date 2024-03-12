package com.marvel.sha.data

import bx.system.Clock
import com.marvel.sha.BuildConfig
import java.security.MessageDigest

internal class MarvelAuthentication(
    private val clock: Clock,
) {
    operator fun invoke(): Auth {
        val ts = clock.instant().minusMillis(1710091030000).toEpochMilli().toString()
        val private = PRIVATE_API_KEY
        val public = PUBLIC_API_KEY
        val input = ts + private + public
        val hash = md5(input)
        return Auth(ts, public, hash)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun md5(input: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val output = digest.digest(input.toByteArray())
        return output.toHexString()
    }

    private companion object {
        const val PUBLIC_API_KEY = BuildConfig.PUBLIC_API_KEY
        const val PRIVATE_API_KEY = BuildConfig.PRIVATE_API_KEY
    }
}
