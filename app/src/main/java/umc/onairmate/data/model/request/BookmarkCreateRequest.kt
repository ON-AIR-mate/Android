package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName

data class BookmarkCreateRequest (
    @SerializedName("roomId")
    val roomId : Int,
    @SerializedName("message")
    val message : String,
)