package umc.OnAirMate.data.repository.repositoryImpl

import umc.OnAirMate.data.api.HomeService
import umc.OnAirMate.data.repository.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl  @Inject constructor(
    val api : HomeService
): HomeRepository {

}