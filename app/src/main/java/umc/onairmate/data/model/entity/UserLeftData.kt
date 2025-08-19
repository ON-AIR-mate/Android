package umc.onairmate.data.model.entity

data class UserLeftData(
    val leftUser: String,
    val role: String,
    val roomParticipants: List<ParticipantData>
)

enum class ParticipantRole(val apiName: String) {
    HOST("host"),
    PARTICIPANT("participant")
}