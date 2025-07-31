package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("username")
    val username : String,
    @SerializedName("password")
    val password : String
)
