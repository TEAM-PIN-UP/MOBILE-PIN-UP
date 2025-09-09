package com.pinup.pinup.util

object Const {
    object Url {
        const val TERM_SERVICE = "https://www.google.com"
        const val TERM_PRIVATE = "https://www.google.com"
        const val TERM_LOCATION = "https://www.google.com"
        const val TERM_MARKETING = "https://www.google.com"
        const val CONTACT_US_URL = "https://docs.google.com/forms/d/e/1FAIpQLSeK6a5W9HdN4tD9C-TQ3G-jMe0KoaoUvKgQ0nn8h79oUdAqtg/viewform"
        const val SUGGESTION_URL = "https://docs.google.com/forms/d/e/1FAIpQLSfY1WnZg-hPhwxW-7AYmF4X6-fwU0pDWyUilAvcGFFvvJfzaw/viewform"
    }

    object PRegex {
        const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        const val PASSWORD_REGEX = "^(?=.*[^A-Za-z0-9]).{8,}$"

        fun maskEmail(email: String, keep: Int = 1, maskChar: Char = '*'): String {
            val match = Regex(EMAIL_REGEX).matchEntire(email) ?: return email
            val (local, domain) = match.destructured

            val shown = local.take(keep.coerceAtLeast(0))
            val maskedCount = (local.length - keep).coerceAtLeast(0)
            val maskedLocal = shown + maskChar.toString().repeat(maskedCount)

            return "$maskedLocal@$domain"
        }
    }
}