package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class ImageResponse (
    @SerializedName("message")
    val message : String,
    @SerializedName("profileImage")
    val profileImage : String
)