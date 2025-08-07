package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

class NicknameResponse {
    @SerializedName("nickname")
    val accessToken : String,
    @SerializedName("profileImage")
    val refreshToken : String
}