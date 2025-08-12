package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class NicknameData(
    @SerializedName("available")
    val available: Boolean,

    @SerializedName("message")
    val message: String
)