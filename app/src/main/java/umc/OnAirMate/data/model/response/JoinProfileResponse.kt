package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class JoinProfileResponse (
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message : String
): Serializable