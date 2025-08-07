package umc.onairmate.ui.lounge.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long,
    val nickname: String,
    val profileImage: String
) : Parcelable
