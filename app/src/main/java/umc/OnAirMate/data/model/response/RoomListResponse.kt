package umc.OnAirMate.data.model.response

import com.google.gson.annotations.SerializedName
import umc.OnAirMate.data.model.entity.RoomData

data class RoomListResponse(
    @SerializedName("continueWatching")
    val continueWatching : List<RoomData>,
    @SerializedName("onAirRooms")
    val onAirRooms : List<RoomData>
)