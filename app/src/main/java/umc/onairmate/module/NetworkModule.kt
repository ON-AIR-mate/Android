package umc.onairmate.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import umc.onairmate.OnAirMateApplication
import umc.onairmate.R
import umc.onairmate.BuildConfig
import umc.onairmate.data.api.JoinService
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseRetrofit

    const val NETWORK_EXCEPTION_OFFLINE_CASE = "network status is offline"
    const val NETWORK_EXCEPTION_BODY_IS_NULL = "result.json body is null"

    @Provides
    @Singleton
    @BaseRetrofit
    fun provideOKHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else  HttpLoggingInterceptor.Level.NONE
        }

        val closeInterceptor = Interceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("Connection", "close").build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addNetworkInterceptor(closeInterceptor)
            .retryOnConnectionFailure(false)
            .build()
    }

    @Provides
    @Singleton


    private inline fun <reified T> Retrofit.buildService(): T{
        return this.create(T::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(OnAirMateApplication.getString(R.string.base_url))  // 수정 필요
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideJoinService(retrofit: Retrofit): JoinService {
            return retrofit.create(JoinService::class.java)
        }
    }

}
