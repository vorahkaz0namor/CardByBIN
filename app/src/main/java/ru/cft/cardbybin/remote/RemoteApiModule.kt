package ru.cft.cardbybin.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.cft.cardbybin.BuildConfig
import ru.cft.cardbybin.util.UnsafeOkHttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteApiModule {
    @Singleton
    @Provides
    fun provideClient(): OkHttpClient =
        UnsafeOkHttpClient.getUnsafeOkHttpClient()

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ) : Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideCardRemoteApi(
        retrofit: Retrofit
    ) : CardRemoteApi =
        retrofit.create()
}