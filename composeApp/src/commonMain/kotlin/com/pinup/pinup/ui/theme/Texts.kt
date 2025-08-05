package com.pinup.pinup.ui.theme


import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

object Texts {
    object Word {
        const val WORD_START = "시작하기"
        const val WORD_ID = "아이디"
        const val WORD_PASSWORD = "비밀번호"
        const val WORD_LOGIN = "로그인"
    }

    object Onboarding{
        val ONBOARDING_TEXT = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.White)) {
                append("우리의 로컬 아카이브, ")
            }
            withStyle(style = SpanStyle(color = Colors.Main)) {
                append("핀업")
            }
        }
        val ONBOARDING_LOGIN_TITLE = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Neutral800)) {
                append("우리의 로컬 아카이브, ")
            }
            withStyle(style = SpanStyle(color = Colors.Main)) {
                append("핀업")
            }
        }
        const val ONBOARDING_LOGIN_TEXT = "로그인하고 나만의 공간을 공유해보세요!"
    }

    object Login{
        const val LOGIN_ID_FIND = "아이디 찾기"
        const val LOGIN_PASSWORD_FIND = "비밀번호 찾기"
        const val LOGIN_SNS = "SNS 로그인"
    }
}