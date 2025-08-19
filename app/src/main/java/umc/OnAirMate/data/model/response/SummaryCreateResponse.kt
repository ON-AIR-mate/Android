package umc.onairmate.data.model.response

import umc.onairmate.data.model.entity.EmotionAnalysis

data class SummaryCreateResponse(
    val emotionAnalysis: List<EmotionAnalysis>,
    val roomTitle: String,
    val summaryId: String,
    val timestamp: String,
    val topicSummary: String,
    val videoTitle: String
)