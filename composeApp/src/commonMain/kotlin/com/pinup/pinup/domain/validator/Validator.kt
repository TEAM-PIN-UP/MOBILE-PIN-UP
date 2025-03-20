package com.pinup.pinup.domain.validator

import java.util.regex.Pattern

object NickNameValidator {
    /**
     * 영어,한글만 허용
     * \u318d\u119E\u11A2\u2022\u2025a\u00B7\uFE55: 천지인 키보드 middle dot 문자
     */
    fun checkNameValidation(name: String): Boolean {
        if (name.isBlank()) return true
        return Pattern.compile("^[a-zA-Zㄱ-ㅎ가-흐ㄱ-ㅣ가-힣ᆢᆞ\\u318d\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$").matcher(name).matches()
    }
}