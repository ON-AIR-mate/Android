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
        //api 호출
        val rawResponse = apiCall()

        // 성공시 응답값 저장
        if (rawResponse.success) {
            DefaultResponse.Success(
                rawResponse.data ?: throw IllegalStateException("Success response should contain data"),
                rawResponse.timestamp)
        } else {
            DefaultResponse.Error(
                code = rawResponse.error?.code,
                message = rawResponse.error?.message ?: "알 수 없는 오류",
                timestamp = rawResponse.timestamp
            )
        }

    } catch (http: HttpException) {
        // 상태 코드 확인
        val code = http.code()
        val parsed = ErrorParser.parseHttpException(http)
        DefaultResponse.Error(
            code = parsed.code ?: code.toString(),
            message = parsed.message,
            timestamp = parsed.timestamp
        )
    } catch (e: Exception) {
        // api 요청 실패
        DefaultResponse.Error(message = "네트워크 오류: ${e.localizedMessage}")
    }
}
