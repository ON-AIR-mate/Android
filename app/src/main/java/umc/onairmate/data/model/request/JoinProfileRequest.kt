package umc.onairmate.data.model.request

data class JoinProfileRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val profileImage: String,
    val agreements: Agreements
) {
    data class Agreements(
        val serviceTerms: Boolean,
        val privacyCollection: Boolean,
        val privacyPolicy: Boolean,
        val marketingConsent: Boolean,
        val eventPromotion: Boolean,
        val serviceNotification: Boolean,
        val advertisementNotification: Boolean,
        val nightNotification: Boolean
    )
}