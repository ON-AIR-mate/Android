package umc.onairmate.data.repository.repository

import retrofit2.Response
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.data.model.response.DefaultResponse

interface TestRepository {
    suspend fun signUp( body : TestRequest ) : Response<DefaultResponse<String>>
}