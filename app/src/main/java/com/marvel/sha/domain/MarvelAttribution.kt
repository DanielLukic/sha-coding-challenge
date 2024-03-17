package com.marvel.sha.domain

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow

internal class MarvelAttribution(context: Context) {

    private val prefs = context.getSharedPreferences("MarvelAttribution", Context.MODE_PRIVATE)

    var value: String
        get() = prefs.getString("attribution", "") ?: "Loading attribution..."
        set(value) = prefs.edit().putString("attribution", value).apply().also { flow.value = value }

    val flow by lazy { MutableStateFlow(value) }

}
