package umc.onairmate.ui.join.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import umc.onairmate.data.model.request.TestRequest.Agreement

data class JoinCollection(
    @SerializedName("username")
    val username : String,
    @SerializedName("password")
    val password : String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("agreements")
    val agreements : Agreement
)
@Parcelize
data class Agreements(
    @SerializedName("serviceTerms")
    val serviceTerms : Boolean = true,
    @SerializedName("privacyCollection")
    val privacyCollection : Boolean = true,
    @SerializedName("privacyPolicy")
    val privacyPolicy : Boolean = true,
    @SerializedName("marketingConsent")
    val marketingConsent : Boolean = true,
    @SerializedName("eventPromotion")
    val eventPromotion : Boolean = true,
    @SerializedName("serviceNotification")
    val serviceNotification : Boolean = false,
    @SerializedName("advertisementNotification")
    val advertisementNotification : Boolean = false,
    @SerializedName("nightNotification")
    val nightNotification : Boolean = false
): Parcelable