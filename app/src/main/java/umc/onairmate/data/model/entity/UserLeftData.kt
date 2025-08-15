package umc.onairmate.data.model.entity

data class UserLeftData(
    val leftUser: String,
    val isHost: Boolean,
    val roomParticipants: List<ParticipantData>
)
