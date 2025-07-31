package umc.onairmate.data.model.response

data class RawDefaultResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: ErrorBody?,
    val timestamp: String?
){
    data class ErrorBody(
        val code: String?,
        val message: String?
    )
}



