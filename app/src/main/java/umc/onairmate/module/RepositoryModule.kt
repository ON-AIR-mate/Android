package umc.onairmate.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import umc.onairmate.data.api.FriendService
import umc.onairmate.data.api.ChatRoomService
import umc.onairmate.data.api.HomeService
import umc.onairmate.data.api.NicknameService
import umc.onairmate.data.api.TestService
import umc.onairmate.data.repository.NicknameRepository
import umc.onairmate.data.repository.repository.FriendRepository
import umc.onairmate.data.repository.repository.ChatRoomRepository
import umc.onairmate.data.repository.repository.HomeRepository
import umc.onairmate.data.repository.repository.TestRepository
import umc.onairmate.data.repository.repositoryImpl.FriendRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.ChatRoomRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.HomeRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.NicknameRepositoryImpl
import umc.onairmate.data.repository.repositoryImpl.TestRepositoryImpl

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
    fun providesTestRepository(
        testService: TestService
    ) : TestRepository = TestRepositoryImpl(testService)

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
    fun providesNicknameRepository(
        nicknameService: NicknameService
    ) : NicknameRepository = NicknameRepositoryImpl(nicknameService)
}
