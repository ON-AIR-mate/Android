package umc.onairmate.ui.lounge.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(
    val title: String,
    val dateCreated: String,
    val lastUpdated: String,
    val privacy: String,
    val thumbnailUrl: String,
    val ownerName: String = "홍길동",
    val date: String,
    val visibility: String,
    val description: String? = null
) : Parcelable
