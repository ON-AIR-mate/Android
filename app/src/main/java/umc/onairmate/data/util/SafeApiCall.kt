package umc.onairmate.data.util


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.RawDefaultResponse

suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> RawDefaultResponse<T>
): DefaultResponse<T> = withContext(Dispatchers.IO) {
    try {
        // 1) RawDefaultResponse 호출
        val rawResponse = apiCall()

        // 2) success 여부 확인
        if (rawResponse.success) {
            DefaultResponse.Success(rawResponse.data!!, rawResponse.timestamp)
        } else {
            DefaultResponse.Error(
                code = rawResponse.error?.code,
                message = rawResponse.error?.message ?: "알 수 없는 오류",
                timestamp = rawResponse.timestamp
            )
        }

    } catch (http: HttpException) {
        // 3) 상태 코드 확인 가능
        val code = http.code()
        val parsed = ErrorParser.parseHttpException(http)
        DefaultResponse.Error(
            code = parsed.code ?: code.toString(),
            message = parsed.message,
            timestamp = parsed.timestamp
        )
    } catch (e: Exception) {
        // 4) 네트워크 자체 실패
        DefaultResponse.Error(message = "네트워크 오류: ${e.localizedMessage}")
    }
}
