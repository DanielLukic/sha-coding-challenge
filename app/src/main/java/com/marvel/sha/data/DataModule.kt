package com.marvel.sha.data

import android.content.Context
import androidx.room.Room
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.marvel.sha.Database
import com.marvel.sha.data.characters.CharacterRepository
import com.marvel.sha.data.characters.CharacterService
import com.marvel.sha.data.comics.ComicsRepository
import com.marvel.sha.data.comics.ComicsService
import com.marvel.sha.data.creators.CreatorRepository
import com.marvel.sha.data.creators.CreatorService
import commarvel.sha.datacomics.ComicsQueries
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal object DataModule {

    operator fun invoke() = module {

        single { comicsDatabase(get()) }
        single { roomDatabase(get()) }

        single { ktor() }
        single { okhttp() }

        singleOf(::CharacterRepository)
        singleOf(::CharacterService)
        singleOf(::ComicsRepository)
        singleOf(::ComicsService)
        singleOf(::CreatorRepository)
        singleOf(::CreatorService)
        singleOf(::MarvelAuthentication)

    }

    private fun comicsDatabase(context: Context) = ComicsQueries(
        AndroidSqliteDriver(
            schema = Database.Schema,
            context = context,
            name = "comics.db",
        )
    )

    private fun ktor() = HttpClient(OkHttp) {
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

    private fun okhttp() = OkHttpClient.Builder()
        .callTimeout(1, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = BASIC })
        .build()

    private fun roomDatabase(context: Context) = Room.databaseBuilder(
        context = context,
        klass = ShaRoomDatabase::class.java,
        name = "room.db",
    ).fallbackToDestructiveMigration().build()

}
