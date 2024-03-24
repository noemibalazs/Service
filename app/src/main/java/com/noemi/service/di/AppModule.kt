package com.noemi.service.di

import android.content.Context
import androidx.work.WorkManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.noemi.service.BuildConfig
import com.noemi.service.util.BASE_URL
import com.noemi.service.workmanager.datasource.ImgurRemoteDataSource
import com.noemi.service.workmanager.datasource.ImgurRemoteDataSourceImpl
import com.noemi.service.workmanager.network.ImgurAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import javax.inject.Singleton
import kotlin.jvm.Throws

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    internal class Authorization : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val header = chain.request().newBuilder()
                .addHeader("Authorization", "Client-ID ${BuildConfig.IMGUR_ID}").build()
            return chain.proceed(header)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(Authorization()).build()
    }

    @Provides
    @Singleton
    fun provideImgurAPI(client: OkHttpClient): ImgurAPI {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build().create(ImgurAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesImgurRemoteDataSource(imgurAPI: ImgurAPI, @ApplicationContext context: Context): ImgurRemoteDataSource =
        ImgurRemoteDataSourceImpl(imgurAPI, context)

    @Provides
    @Singleton
    fun providesWorkManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)
}