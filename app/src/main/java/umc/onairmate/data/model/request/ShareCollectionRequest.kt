package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName

data class ShareCollectionRequest(
    @SerializedName("friendIds")
    val friendIds: List<Int>
)
