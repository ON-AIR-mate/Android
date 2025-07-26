package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.HomeService
import umc.onairmate.data.repository.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl  @Inject constructor(
    val api : HomeService
): HomeRepository {

}