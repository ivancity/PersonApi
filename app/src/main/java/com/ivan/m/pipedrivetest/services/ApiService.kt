package com.ivan.m.pipedrivetest.services

import com.ivan.m.pipedrivetest.utils.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiService {
    private val authInterceptor = Interceptor {chain->
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("api_token", Constants.API_TOKEN)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val pipedriveClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .build()

    private fun provideMoshi() : Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(pipedriveClient)
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val pipeDriveApi: PipeDriveApi = retrofit().create(PipeDriveApi::class.java)
}