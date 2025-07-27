package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse (
    @SerializedName("message")
    val message : String
)