package umc.onairmate.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import umc.onairmate.data.api.AuthService
import umc.onairmate.data.api.BookmarkService
import umc.onairmate.data.api.FriendService
import umc.onairmate.data.api.ChatRoomService
import umc.onairmate.data.api.HomeService
import umc.onairmate.data.api.ImageService
import umc.onairmate.data.api.JoinService
import umc.onairmate.data.repository.repository.BookmarkRepository
import umc.onairmate.data.repository.repository.FriendRepository
import umc.onairmate.data.repository.repository.ChatRoomRepository
import umc.onairmate.data.repository.repository.HomeRepository
import umc.onairmate.data.repository.repository.ImageRepository
import umc.onairmate.data.repository.repository.JoinRepository
import umc.onairmate.data.repository.repository.AuthRepository
import umc.onairmate.data.repository.repositoryImpl.BookmarkRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.FriendRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.ChatRoomRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.HomeRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.ImageRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.JoinRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.AuthRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun providesHomeRepository(
        homeService: HomeService
    ) : HomeRepository = HomeRepositoryImpl(homeService)

    @ViewModelScoped
    @Provides
    fun providesAuthRepository(
        authService: AuthService
    ) : AuthRepository = AuthRepositoryImpl(authService)

    @ViewModelScoped
    @Provides
    fun providesFriendRepository(
        friendService: FriendService
    ) : FriendRepository = FriendRepositoryImpl(friendService)

    @ViewModelScoped
    @Provides
    fun providesChatRoomRepository(
        chatRoomService: ChatRoomService
    ) : ChatRoomRepository = ChatRoomRepositoryImpl(chatRoomService)

    @ViewModelScoped
    @Provides
    fun providesBookmarkRepository(
        bookmarkService: BookmarkService
    ) : BookmarkRepository = BookmarkRepositoryImpl(bookmarkService)

    @ViewModelScoped
    @Provides
    fun providesImageRepository(
        imageService: ImageService
    ) : ImageRepository = ImageRepositoryImpl(imageService)

    @Provides
    @ViewModelScoped
    fun provideJoinRepository(
        joinService: JoinService
    ): JoinRepository {
        return JoinRepositoryImpl(joinService)
    }

}
