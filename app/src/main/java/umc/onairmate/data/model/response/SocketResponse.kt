package umc.onairmate.data.model.response

data class SocketResponse<T>(
    val type: String,
    val data: T
)
