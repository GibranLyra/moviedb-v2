package com.gibranlyra.moviedbservice

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal const val BASE_URL = "https://api.themoviedb.org/4/"
internal const val API_KEY_NAME = "api_key"
internal const val API_KEY_VALUE = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2Y2I2Y2E2YjA4N2Q5ZjdjZWQ2MTg0MTc4MjU4YzM2NCIsInN1YiI6IjVhNzg4MDA1MGUwYTI2NTk5MTAxNGJlZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.PgP8iO-jrPO7hv_j--QZIc7LV-r1S0nFJOBzn3yNX-g"

internal const val READ_TIMEOUT_DEFAULT: Long = 60
internal const val CONNECT_TIMEOUT_DEFAULT: Long = 60

object MovieDbApiModule {
    lateinit var retrofit: Retrofit private set

    private val gson: Gson by lazy {
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .create()
    }

    fun buildRetrofit(logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY) {
        val builder = buildClient(logLevel, READ_TIMEOUT_DEFAULT, CONNECT_TIMEOUT_DEFAULT)

        builder.addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().header("Authorization", "Bearer $API_KEY_VALUE").build())
        }

        builder.addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().header("Content-Type", "application/json;charset=utf-8").build())
        }

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun buildClient(logLevel: HttpLoggingInterceptor.Level,
                            readTimeout: Long,
                            connectTimeout: Long): OkHttpClient.Builder {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = logLevel

        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
    }
}
