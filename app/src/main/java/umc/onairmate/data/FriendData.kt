package umc.onairmate.data

import com.google.gson.annotations.SerializedName

data class FriendData(
    @SerializedName("userId")
    val userId : Int,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("popularity")
    val popularity : Int,
    @SerializedName("isOnline")
    val isOnline : Boolean
)
