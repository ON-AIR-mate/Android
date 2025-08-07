package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.NicknameService
import umc.onairmate.data.repository.NicknameRepository
import javax.inject.Inject

class NicknameRepositoryImpl @Inject constructor(
    private val api: NicknameService
): NicknameRepository {}