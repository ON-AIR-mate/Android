package umc.onairmate.ui.login

data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)