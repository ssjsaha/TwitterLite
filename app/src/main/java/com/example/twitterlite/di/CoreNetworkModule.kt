package com.sevenpeakssoftware.base.di

import android.content.Context
import com.assessment.base.network.ThrowableAdapter
import com.sevenpeakssoftware.base.network.ResponseAdapterFactory
import com.sevenpeakssoftware.base.network.UserInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreNetworkModule {
    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir, "offlineCache")
        return Cache(httpCacheDirectory, (10 * 1024 * 1024).toLong())
    }

    @Singleton
    @Provides
    fun provideLoggerInterceptor() = run {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideUserInterceptor() = UserInterceptor()

    @Singleton
    @Provides
    fun provideResponseAdapter() = ResponseAdapterFactory()

    @Singleton
    @Provides
    fun provideThrowableAdapter() = ThrowableAdapter()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        userInterceptor: UserInterceptor,
    ) = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .addInterceptor(userInterceptor)
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()

    @Singleton
    @Provides
    fun provideMoshi(throwableAdapter: ThrowableAdapter): Moshi = Moshi.Builder()
        .add(throwableAdapter)
        .build()

    @Singleton
    @Provides
    fun createRetrofit(
        client: OkHttpClient,
        moshi: Moshi,
        responseAdapterFactory: ResponseAdapterFactory,
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://www.apphusetreach.no/application/119267/")
        .addCallAdapterFactory(responseAdapterFactory)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

