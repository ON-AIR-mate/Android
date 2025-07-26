package umc.onairmate.data.model.response

data class DefaultResponse<T> (
    val success: Boolean,
    val data: T?, // 성공 시에만 존재
    val error: ErrorResponse? // 실패 시에만 존재
){
    data class ErrorResponse(
        val code: String,
        val message: String
    )
}