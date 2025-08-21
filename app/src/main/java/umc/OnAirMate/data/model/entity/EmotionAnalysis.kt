package umc.onairmate.data.model.entity

import umc.onairmate.R

data class EmotionAnalysis(
    val emotion: String,
    val percentage: Int
)

enum class Emotion(val emotionName: String, val emotionColor: Int) {
    JOY("기쁨", R.color.happy),
    SADNESS("슬픔", R.color.sadness),
    ANGER("분노", R.color.anger),
    SURPRISE("놀람", R.color.surprise),
    TOUCHED("감동", R.color.touched),
    EMPATHY("공감", R.color.empathy),
    FEAR("공포", R.color.fear),
    FRUSTRATION("좌절", R.color.frustration),
    DESPAIR("절망", R.color.despair),
    EMBARRASSMENT("당황", R.color.embarrassment);

    companion object {
        fun emotionToApiName(emotion: Emotion): String {
            return emotion.emotionName
        }
        fun apiNameToEmotion(apiName: String): Emotion? {
            return Emotion.entries.firstOrNull { apiName == it.emotionName }
        }
    }
}