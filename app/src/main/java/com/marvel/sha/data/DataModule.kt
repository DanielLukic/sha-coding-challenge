package com.marvel.sha.data

import android.content.Context
import androidx.room.Room
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.marvel.sha.Database
import com.marvel.sha.data.ktordelight.ComicRepository
import com.marvel.sha.data.ktordelight.ComicService
import commarvel.sha.datacomics.ComicsQueries
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
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

        singleOf(::ComicRepository)
        singleOf(::ComicService)
        singleOf(::MarvelAuthentication)
        singleOf(::MarvelRepository)
        singleOf(::MarvelService)

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
    }

    private fun okhttp() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = BASIC })
        .build()

    private fun roomDatabase(context: Context) = Room.databaseBuilder(
        context = context,
        klass = ShaRoomDatabase::class.java,
        name = "room.db",
    ).fallbackToDestructiveMigration().build()

}
