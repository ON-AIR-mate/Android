package umc.onairmate.ui.lounge.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Collection(
    val title: String,
    val dateCreated: String,
    val lastUpdated: String,
    val privacy: String,
    val thumbnailUrl: String
)
