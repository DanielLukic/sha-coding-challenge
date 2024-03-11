package com.marvel.sha.data

import android.content.Context
import androidx.room.Room
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal object DataModule {

    operator fun invoke() = module {

        fun roomDb(context: Context) = Room.databaseBuilder(
            context = context,
            klass = RoomShaDatabase::class.java,
            name = "sha.db",
        ).fallbackToDestructiveMigration().build()

        fun retrofit() = RetrofitService.create()

        fun ktor() = HttpClient(OkHttp) {
            engine {
                config {
                    followRedirects(false)
                    followSslRedirects(false)
                }
                //addInterceptor(...)
                //addNetworkInterceptor(...)
                //preconfigured =
            }
        }

        single { roomDb(get()) }
        single { retrofit() }
        single { ktor() }

        singleOf(::RetrofitRoomRepo)

    }

}
