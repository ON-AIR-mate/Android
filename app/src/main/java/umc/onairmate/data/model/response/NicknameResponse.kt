package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName
import umc.onairmate.data.model.entity.NicknameData

class NicknameResponse (
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: NicknameData
)