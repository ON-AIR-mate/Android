package umc.onairmate.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import umc.onairmate.data.api.AuthService
import umc.onairmate.data.api.BookmarkService
import umc.onairmate.data.api.ChatRoomService
import umc.onairmate.data.api.CollectionService
import umc.onairmate.data.api.FriendService
import umc.onairmate.data.api.HomeService
import umc.onairmate.data.api.JoinService
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
    fun authApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): AuthService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun chatRoomApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): ChatRoomService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun friendApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): FriendService{
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun bookmarkApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): BookmarkService{
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun collectionApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): CollectionService{
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun joinApi(@NetworkModule.BaseRetrofit retrofit: Retrofit): JoinService {
        return retrofit.buildService()
    }
}