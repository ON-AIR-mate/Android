package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName
import umc.onairmate.data.model.entity.BookmarkData

data class BookmarkListResponse(
    @SerializedName("uncategorized")
    val uncategorized : List<BookmarkData>,
    @SerializedName("all")
    val all : List<BookmarkData>
)
