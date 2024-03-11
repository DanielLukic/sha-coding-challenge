package com.marvel.sha

import android.app.Application
import bx.logging.AndroidLogSink
import bx.logging.Log
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ShaMain : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.level = Log.Level.VERBOSE
        Log.sink = AndroidLogSink()
        Log.tag = "SHA"

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@ShaMain)
            modules(AppModule())
        }
    }

}
