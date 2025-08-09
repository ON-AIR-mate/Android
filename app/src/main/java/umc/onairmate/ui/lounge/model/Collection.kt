package umc.onairmate.ui.lounge.model

import java.io.Serializable

data class Collection(
    val title: String,
    val dateCreated: String,
    val lastUpdated: String,
    val privacy: String,
    val thumbnailUrl: String
):Serializable
