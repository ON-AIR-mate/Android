package umc.onairmate.data.model.entity

data class ParticipantData(
    val isHost: Boolean,
    val joinedAt: String,
    val nickname: String,
    val popularity: Int,
    val profileImage: String,
    val userId: Int
)