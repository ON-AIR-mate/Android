package umc.onairmate.ui.util

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object TimeFormatter {
    fun formatRelativeTime(isoString: String): String {
        // ISO8601 문자열 파싱
        val parsedTime = try {
            Instant.parse(isoString)
        }catch (e: Exception){
            return "알 수 없음"
        }

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

    // 알림용
    fun formatRelativeTimeNotification(isoString: String): String {
        // ISO8601 문자열 파싱
        val parsedTime = try {
            Instant.parse(isoString)
        } catch (e: Exception) {
            return "알 수 없음"
        }

        // 현재 시간
        val now = Instant.now()

        // 시간 차이 계산
        val duration = Duration.between(parsedTime, now)
        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        // 규칙에 맞춰 반환
        return when {
            minutes < 10 -> "방금 전"
            minutes < 60 -> "${minutes}분 전"
            hours < 24 -> "${hours}시간 전"
            days <= 7 -> "${days}일 전"
            else -> "오래 전"
        }
    }

    // 컬렉션 생성/수정 날짜 데이터 포매팅 yyyy.MM.dd 형태
    fun formatCollectionDate(isoString: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val date = LocalDate.parse(isoString, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            outputFormatter.format(date)
        } catch (e: DateTimeParseException) {
            isoString
        }
    }
}