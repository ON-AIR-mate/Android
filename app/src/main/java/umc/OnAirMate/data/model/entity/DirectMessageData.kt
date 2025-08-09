package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class DirectMessageData (
    @SerializedName("sender")
    val sender : String,
    @SerializedName("message")
    val message : String
)