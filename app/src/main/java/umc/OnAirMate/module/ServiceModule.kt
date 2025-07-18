package umc.OnAirMate.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import umc.OnAirMate.data.api.HomeService


@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    private inline fun <reified T> Retrofit.buildService(): T {
        return this.create(T::class.java)
    }
    @Provides
    fun homeApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): HomeService {
        return retrofit.buildService()
    }
}