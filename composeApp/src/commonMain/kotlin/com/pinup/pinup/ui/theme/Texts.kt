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
        const val PIN_MAP = "핀맵"
        const val PINLOG = "핀로그"
        const val FEED = "피드"
        const val ARTICLE = "아티클"
        const val MY = "마이"
    }

    object Onboarding {
        val ONBOARDING_TEXT = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Gray50)) {
                append("우리의 로컬 아카이브, ")
            }
            withStyle(style = SpanStyle(color = Colors.Main)) {
                append("핀업")
            }
        }
        val ONBOARDING_LOGIN_TITLE = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Gray800)) {
                append("우리의 로컬 아카이브, ")
            }
            withStyle(style = SpanStyle(color = Colors.Main)) {
                append("핀업")
            }
        }
        const val ONBOARDING_LOGIN_TEXT = "로그인하고 나만의 공간을 공유해보세요!"
    }

    object Login {
        const val LOGIN_ID_FIND = "아이디 찾기"
        const val LOGIN_SNS = "SNS 로그인"
        const val INCORRECT_ID = "아이디 혹은 비밀번호가 일치하지 않습니다."
    }

    object FindPassword {
        const val PASSWORD_HINT = "비밀번호는 8~20자, 특수문자 포함 필수입니다."
        const val CHANGE_PASSWORD = "비밀번호 변경하기"
        const val PASSWORD_AGAIN_HINT = "비밀번호 확인"
    }

    object SignupEmail {
        const val TITLE = "로그인에 사용할\n이메일을 입력해주세요."
        const val HINT = "이메일 주소를 입력해주세요."
        const val INVALID = "유효한 이메일 주소가 아닙니다."
        const val DUPLICATE = "동일한 이메일 주소로 가입된 계정이 있습니다.\n기존 계정을 확인해주세요."
        const val CODE_HINT = "인증번호를 입력해주세요."
        const val CODE_INVALID = "인증번호가 일치하지 않습니다."
        const val SEND_CODE = "인증번호가 전송되었습니다."
    }

    object SignupPassword {
        const val TITLE = "사용하실 비밀번호를\n입력해주세요."
        const val HINT = "비밀번호 입력 (8자이상 + 특수문자 포함)"
        const val INVALID = "비밀번호는 8~20자, 특수문자 포함 필수입니다."
        const val CONFIRM = "비밀번호 확인"
        const val NOT_MATCH = "입력된 비밀번호가 일치하지 않습니다."
    }

    object SignupTerms {
        const val TITLE = "서비스 이용약관에\n동의해주세요."
        const val ALL_AGREE = "약관 전체 동의"
        const val SERVICE_AGREE = "이용약관 동의 (필수)"
        const val PRIVATE_INFO_AGREE = "개인정보 수집 및 이용 동의 (필수)"
        const val LOCATION_INFO_AGREE = "위치정보 이용 동의 (필수)"
        const val MARKETING_AGREE = "홍보 및 마케팅 이용 동의 (선택)"
    }

    object SignupNickname {
        const val NICKNAME_TITLE = "반가워요!\n닉네임을 만들어볼까요?"
        const val INPUT_NICKNAME = "닉네임 입력"
        const val NICKNAME_HINT = "닉네임은 나중에 언제든지 변경가능해요."
        const val CONDITION_NICKNAME = "한글, 영문만 입력 가능"
        const val DUPLICATE_NICKNAME = "중복되는 닉네임이에요."
    }

    object SignupProfile {
        const val PROFILE_TITLE = "프로필에 사용 될\n이미지를 골라주세요."
        const val PROFILE_REGISTER = "멋진 프로필\n사진이 등록되었어요!"
        const val PROFILE_SELECT = "사진 선택하기"
    }

    object PinMap {
        const val SEARCH_HINT = "장소/위치 검색하기"
        const val EMPTY_PINLOG = "아직 핀로그가 없어요!\n나만의 장소를 핀업해보세요:)"
        const val SEARCH_RESULT = "검색 결과"
        const val GO_ARTICLE = "아티클 바로가기"
    }

    object PinLog {
        const val WRITE_PINLOG = "핀로그 작성"
        const val WRITE_TITLE = "어떤 장소의 핀로그를\n작성할까요?"
        const val WRITE_DESCRIPTION = "작성된 핀로그는 핀버디만 볼 수 있어요"
        const val SEARCH_HINT = "장소 검색하기"
        const val SEARCH_RESULT = "검색 결과"
    }
}