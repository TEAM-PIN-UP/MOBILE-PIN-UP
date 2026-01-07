package com.pinup.placePinup.domain.model

enum class FCMType {

    // 소셜/커뮤니티 관련
    FRIEND_LOG_CREATED,
    LOG_LIKE,
    LOG_COMMENT,
    FRIEND_LOG_SAME_PLACE,
    FRIEND_REQUEST,
    FRIEND_REQUEST_ACCEPTED,

    // 앱 기능/이벤트
    SUMMARY_WEEKLY,
    SUMMARY_MONTHLY,
    FEATURE_UPDATE,
    MEMORY_REMINDER,
    ANNIVERSARY,
    WEEKLY_RECOMMENDATION,

    // 리마인드/휴면
    DAILY_LOG_REMINDER,
    WEEKLY_LOG_REMINDER,
    DORMANT_USER_REENGAGEMENT;

    companion object {
        fun of(name: String): FCMType {
            return if (FCMType.entries.any { it.name == name }) {
                FCMType.valueOf(name)
            } else {
                FCMType.DAILY_LOG_REMINDER
            }
        }
    }
}