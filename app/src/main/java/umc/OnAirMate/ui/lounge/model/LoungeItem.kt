package umc.onairmate.ui.lounge.model

data class LoungeItem(
    val thumbnailUrl: String,
    val title: String,
    val createdDate: String,
    val visibility: String,
    val description: String = "", // 컬렉션 설명 추가
    val videoList: List<VideoItem>
)
