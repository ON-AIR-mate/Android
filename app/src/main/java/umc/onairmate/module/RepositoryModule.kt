package umc.onairmate.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import umc.onairmate.data.api.HomeService
import umc.onairmate.data.repository.repository.HomeRepository
import umc.onairmate.data.repository.repositoryImpl.HomeRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @ViewModelScoped
    @Provides
    fun providesHomeRepository(
        homeService: HomeService
    ) : HomeRepository = HomeRepositoryImpl(homeService)

}
