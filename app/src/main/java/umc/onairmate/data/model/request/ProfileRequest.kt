package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName

data class ProfileRequest (
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImage")
    val profileImage : String
)