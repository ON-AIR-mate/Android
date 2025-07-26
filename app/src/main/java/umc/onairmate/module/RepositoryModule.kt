package umc.OnAirMate.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import umc.OnAirMate.data.api.HomeService
import umc.OnAirMate.data.repository.repository.HomeRepository
import umc.OnAirMate.data.repository.repositoryImpl.HomeRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @ViewModelScoped
    @Provides
    fun providesHomeRepository(
        homeService: HomeService
    ) : HomeRepository = HomeRepositoryImpl(homeService)

}
