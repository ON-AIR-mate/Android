package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class ParticipantData(
    @SerializedName("isHost")
    val isHost: Boolean,
    @SerializedName("joinedAt")
    val joinedAt: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("popularity")
    val popularity: Int,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("userId")
    val userId: Int
)