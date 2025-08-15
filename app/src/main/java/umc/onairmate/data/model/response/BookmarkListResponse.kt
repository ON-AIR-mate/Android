package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName
import umc.onairmate.data.model.entity.BookmarkData
import umc.onairmate.data.model.entity.RoomArchiveData

data class BookmarkListResponse(
    @SerializedName("uncategorized")
    val uncategorized : List<RoomArchiveData>,
    @SerializedName("all")
    val all : List<RoomArchiveData>
)
