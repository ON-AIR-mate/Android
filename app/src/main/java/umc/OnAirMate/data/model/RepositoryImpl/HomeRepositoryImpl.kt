package umc.OnAirMate.data.model.RepositoryImpl

import umc.OnAirMate.data.api.HomeService
import umc.OnAirMate.data.model.Repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl  @Inject constructor(
    val api : HomeService
): HomeRepository {

}