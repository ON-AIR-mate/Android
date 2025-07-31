package umc.onairmate.data.util

import com.google.gson.Gson
import retrofit2.HttpException
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.RawDefaultResponse

object ErrorParser {
    private val gson = Gson()

    fun parseHttpException(exception: HttpException): DefaultResponse.Error {
        return try {
            val errorBody = exception.response()?.errorBody()?.string()
            if (errorBody.isNullOrEmpty()) {
                DefaultResponse.Error(message = "서버 오류 발생")
            } else {
                val json = gson.fromJson(errorBody, RawDefaultResponse::class.java)
                DefaultResponse.Error(
                    code = json.error?.code,
                    message = json.error?.message ?: "서버 오류 발생",
                    timestamp = json.timestamp
                )
            }
        } catch (e: Exception) {
            DefaultResponse.Error(message = "에러 파싱 실패: ${e.localizedMessage}")
        }
    }
}
