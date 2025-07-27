package umc.onairmate.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.data.model.response.DefaultResponse

interface TestService {
    @POST("/auth/register")
    suspend fun signUp(
        @Body body : TestRequest
    ) : Response<DefaultResponse<String>>
}