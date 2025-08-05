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
        const val NEXT = "다음"
        const val SKIP = "건너뛰기"
        const val EMAIL = "이메일"
        const val CONFIRM = "확인"
        const val VERIFY = "인증하기"
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
        const val INCORRECT_ID = "아이디 혹은 비밀번호가 일치하지 않습니다."
    }

    object SignupEmail{
        const val TITLE = "로그인에 사용할\n이메일을 입력해주세요."
        const val HINT = "이메일 주소를 입력해주세요."
        const val INVALID = "유효한 이메일 주소가 아닙니다."
        const val DUPLICATE = "동일한 이메일 주소로 가입된 계정이 있습니다.\n기존 계정을 확인해주세요."
        const val CODE_HINT = "인증번호를 입력해주세요."
        const val CODE_INVALID = "인증번호가 일치하지 않습니다."
    }
}