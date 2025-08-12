package umc.onairmate.data.model.entity

import java.io.Serializable

data class CollectionData (
    val title: String,
    val dateCreated: String,
    val lastUpdated: String,
    val privacy: String,
    val thumbnailUrl: String
): Serializable
