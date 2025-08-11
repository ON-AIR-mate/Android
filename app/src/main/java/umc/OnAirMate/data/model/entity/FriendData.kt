package umc.onairmate.data.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendData(
    @SerializedName("userId")
    val userId : Int = 0,
    @SerializedName("nickname")
    val nickname : String = "",
    @SerializedName("profileImage")
    val profileImage : String = "",
    @SerializedName("popularity")
    val popularity : Int = 0,
    @SerializedName("isOnline")
    val isOnline : Boolean = false
): Parcelable