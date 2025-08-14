package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName
import umc.onairmate.data.model.entity.CollectionData

data class CollectionListResponse (
    @SerializedName("collections")
    val collections: List<CollectionData>
)