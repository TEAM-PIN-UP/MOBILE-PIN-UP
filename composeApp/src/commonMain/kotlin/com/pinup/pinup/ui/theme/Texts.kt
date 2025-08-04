package com.pinup.pinup.ui.theme


import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

object Texts {
    val ONBOARDING_TEXT = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Colors.White)) {
            append("우리의 로컬 아카이브, ")
        }
        withStyle(style = SpanStyle(color = Colors.Main)) {
            append("핀업")
        }
    }

    val WORD_START = "시작하기"
}