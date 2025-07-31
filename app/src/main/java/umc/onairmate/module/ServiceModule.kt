package umc.onairmate.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import umc.onairmate.data.api.FriendService
import umc.onairmate.data.api.HomeService
import umc.onairmate.data.api.TestService
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    private inline fun <reified T> Retrofit.buildService(): T {
        return this.create(T::class.java)
    }


    @Provides
    @Singleton
    fun homeApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): HomeService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun testApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): TestService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun friendApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): FriendService{
        return retrofit.buildService()
    }
}