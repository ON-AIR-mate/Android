package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("nickname")
    val nickname : String = "",
    @SerializedName("profileImage")
    val profileImage : String = "",
    @SerializedName("popularity")
    val popularity : Int = 0,
    @SerializedName("requestStatus")
    val requestStatus : String? = null
)
