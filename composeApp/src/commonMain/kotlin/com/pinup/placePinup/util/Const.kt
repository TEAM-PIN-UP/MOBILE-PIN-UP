package com.pinup.placePinup.util

object Const {
    object Url {
        const val SERVICE_TERM = "https://www.notion.so/29ecbe66576180ae8abcf3cdf8e6c9d1?source=copy_link"
        const val PERSONAL_TERM = "https://www.notion.so/29ecbe66576180f5b710fb6074483fcd?source=copy_link"
        const val LOCATION_TERM = "https://www.notion.so/29ecbe665761806c9c45f85c4e6443e9?source=copy_link"
        const val MARKETING_TERM = "https://www.notion.so/29ecbe66576180b9b104f5cd76bff6fa"
        const val CONTACT_US_URL = "https://docs.google.com/forms/d/e/1FAIpQLSeK6a5W9HdN4tD9C-TQ3G-jMe0KoaoUvKgQ0nn8h79oUdAqtg/viewform"
        const val SUGGESTION_URL = "https://docs.google.com/forms/d/e/1FAIpQLSfY1WnZg-hPhwxW-7AYmF4X6-fwU0pDWyUilAvcGFFvvJfzaw/viewform"
    }

    object PRegex {
        const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        const val PASSWORD_REGEX = "^(?=.*[^A-Za-z0-9]).{8,}$"

        fun maskEmail(email: String, keep: Int = 1, maskChar: Char = '*'): String {
            if (!Regex(EMAIL_REGEX).matches(email)) return email

            val at = email.indexOf('@')
            if (at <= 0 || at == email.lastIndex) return email

            val local = email.substring(0, at)
            val domain = email.substring(at + 1)

            val k = keep.coerceIn(0, local.length)
            val maskedLocal = local.take(k) + maskChar.toString().repeat(local.length - k)

            return "$maskedLocal@$domain"
        }
    }

    object ShareKey {
        const val KAKAO_USER_ID = "userId"
        const val WEB_LINK = "https://www.youtube.com/watch?v=yWP--1gsr20&list=RDyWP--1gsr20&start_radio=1"
    }

    object NavKey {
        const val PINLOG_WRITE_RESULT = "pinlog_result"
    }
}