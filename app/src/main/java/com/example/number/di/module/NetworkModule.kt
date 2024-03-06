package com.example.number.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.number.R
import com.example.number.data.api.NetworkService
import com.example.number.data.repository.NetworkRepository
import com.example.number.data.repository.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(
        context: Context
    ): OkHttpClient {
        val okhttpBuilder = OkHttpClient.Builder()
            .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(context))
        return okhttpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(context: Context, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(context.getString(R.string.HTTPS_API_URL))
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideNetworkService(retrofit: Retrofit): NetworkService =
        retrofit.create(NetworkService::class.java)

    @Provides
    @Singleton
    fun provideNetworkRepository(
        vendorLoginService: NetworkService
    ): NetworkRepository = NetworkRepositoryImpl(vendorLoginService)

    companion object {
        private const val HTTP_TIMEOUT: Long = 60 // SECONDS
    }
}
