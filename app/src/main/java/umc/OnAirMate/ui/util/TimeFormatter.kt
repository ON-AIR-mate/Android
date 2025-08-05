package umc.onairmate.ui.util

import java.time.Duration
import java.time.Instant

object TimeFormatter {
    fun formatRelativeTime(isoString: String): String {
        // ISO8601 문자열 파싱
        val parsedTime = Instant.parse(isoString)

        // 현재 시간
        val now = Instant.now()

        // 시간 차이 계산 (분 단위 → 시간 단위 변환 용도)
        val duration = Duration.between(parsedTime, now)
        val hours = duration.toHours()
        val days = duration.toDays()

        // 규칙에 맞춰 반환
        return when {
            hours < 24 -> "${hours}시간 전"
            days in 1..7 -> "${days}일 전"
            days in 8..30 -> "${days / 7}주 전"
            else -> "오래 전"
        }
    }
}