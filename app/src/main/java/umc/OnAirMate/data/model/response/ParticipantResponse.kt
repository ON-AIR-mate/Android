package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName
import umc.onairmate.data.model.entity.ParticipantData

data class ParticipantResponse (
    @SerializedName("data")
    val participants : List<ParticipantData>
)