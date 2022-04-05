package com.gesecur.app.data.gesecur

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.gesecur.app.domain.Constants.API_TIMEOUT
import com.gesecur.app.domain.Constants.HEADER_ACCEPT_LANGUAGE
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit


object  GesecurNetworkProvider {

    fun getOkHttpClient(): OkHttpClient {
        with(OkHttpClient.Builder()) {
            connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(getLoggingInterceptor())
            addInterceptor(getInterceptor())

            return build()
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun getRetrofit(baseUrl: String, okHttpClient: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    private fun getLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .build()
    }

    private fun getInterceptor(): Interceptor {
        return Interceptor {
            val request = it.request()
            val builder = request.newBuilder()

            builder.header(HEADER_ACCEPT_LANGUAGE, Locale.getDefault().language)

            it.proceed(builder.build())
        }
    }
}
