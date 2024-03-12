package com.marvel.sha.data

import android.content.Context
import androidx.room.Room
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.marvel.sha.Database
import com.marvel.sha.data.characters.CharactersDatabase
import com.marvel.sha.data.characters.CharactersRepository
import com.marvel.sha.data.characters.CharactersService
import com.marvel.sha.data.comics.ComicsRepository
import com.marvel.sha.data.comics.ComicsService
import commarvel.sha.datacomics.ComicsQueries
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal object DataModule {

    operator fun invoke() = module {

        fun charactersDatabase(context: Context) = Room.databaseBuilder(
            context = context,
            klass = CharactersDatabase::class.java,
            name = "characters.db",
        ).fallbackToDestructiveMigration().build()

        fun comicsDatabase(context: Context) = ComicsQueries(
            AndroidSqliteDriver(
                schema = Database.Schema,
                context = context,
                name = "comics.db",
            )
        )

        fun charactersService() = CharactersService.create()

        fun ktor() = HttpClient(OkHttp) {
            install(ContentNegotiation) { gson() }
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
        single { comicsDatabase(get()) }
        single { ktor() }

        singleOf(::CharactersRepository)
        singleOf(::ComicsRepository)
        singleOf(::ComicsService)
        singleOf(::MarvelAuthentication)

    }

}
