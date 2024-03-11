package com.marvel.sha.data

import android.content.Context
import androidx.room.Room
import com.marvel.sha.data.characters.CharactersDatabase
import com.marvel.sha.data.characters.CharactersRepository
import com.marvel.sha.data.characters.CharactersService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal object DataModule {

    operator fun invoke() = module {

        fun charactersDatabase(context: Context) = Room.databaseBuilder(
            context = context,
            klass = CharactersDatabase::class.java,
            name = "characters.db",
        ).fallbackToDestructiveMigration().build()

        fun charactersService() = CharactersService.create()

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

        single { charactersDatabase(get()) }
        single { charactersService() }
        single { ktor() }

        singleOf(::CharactersRepository)

    }

}
