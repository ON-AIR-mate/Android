package umc.onairmate.data.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class TestRequest(
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
){
    @Parcelize
    data class Agreement(
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
        val serviceNotification : Boolean = true,
        @SerializedName("advertisementNotification")
        val advertisementNotification : Boolean = true,
        @SerializedName("nightNotification")
        val nightNotification : Boolean = true
    ): Parcelable
}
