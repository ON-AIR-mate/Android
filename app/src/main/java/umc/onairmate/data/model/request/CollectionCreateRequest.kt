package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName

data class CollectionCreateRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("visibility")
    val visibility: String
)
