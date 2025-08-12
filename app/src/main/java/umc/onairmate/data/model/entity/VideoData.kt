package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class VideoData(

    @SerializedName("channelName")
    val channelName: String = "",
    @SerializedName("thumbnail")
    val thumbnail: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("uploadTime")
    val uploadTime: String ="",
    @SerializedName("videoId")
    val videoId: String = "",
    @SerializedName("viewCount")
    val viewCount: Long = 0
)