package umc.onairmate.data.model.response


sealed class DefaultResponse<out T> {
    data class Success<out T>(
        val data: T,
        val timestamp: String? = null
    ) : DefaultResponse<T>()

    data class Error(
        val code: String? = null,
        val message: String,
        val timestamp: String? = null
    ) : DefaultResponse<Nothing>()

}

