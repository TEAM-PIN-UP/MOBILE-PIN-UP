package com.pinup.placePinup.ui.theme


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.pinup.placePinup.domain.model.ReportType
import com.pinup.placePinup.domain.model.ReportType.*

object Texts {
    fun buildHighlightedText(
        text: String,
        keyword: String?,
        color: Color,
        ignoreCase: Boolean = true
    ): AnnotatedString {
        if (keyword.isNullOrBlank()) return AnnotatedString(text)

        val builder = AnnotatedString.Builder(text)
        val pattern = Regex(Regex.escape(keyword), if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet())

        pattern.findAll(text).forEach { match ->
            builder.addStyle(
                SpanStyle(
                    color = color,
                ),
                start = match.range.first,
                end = match.range.last + 1
            )
        }
        return builder.toAnnotatedString()
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
        const val CHANGE_PASSWORD_HINT = "임시 비밀번호로 로그인 시, 비밀번호 변경이 필요합니다."
        const val SEND_PASSWORD = "비밀번호 발송"
        const val PASSWORD_AGAIN_HINT = "비밀번호 확인"
        const val FIND_PASSWORD = "비밀번호 찾기"
        const val FIND_PASSWORD_HINT = "입력한 주소로 임시 비밀번호가 전송됩니다."
        const val SENT_PASSWORD_TITLE = "이메일이 전송되었습니다!"
        const val SENT_PASSWORD_HINT = "이메일로 전송 받은 임시 비밀번호로 로그인 해주세요."
        const val DO_LOGIN = "로그인하기"
    }

    object FindId {
        const val FIND_ID = "아이디 찾기"
        const val INPUT_NICKNAME = "닉네임 입력"
        const val FIND_ID_BY_EMAIL = "이메일 주소로 찾기"
        const val FIND_ID_BY_NICKNAME = "닉네임으로 찾기"
        const val FIND_ID_RESULT = "해당 이메일로 가입된 계정이 있습니다.\n비밀번호를 변경하고 싶으신가요?"
        const val NOT_EXIST_NICKNAME = "존재하지 않는 닉네임입니다."
        fun getNickNameTitle(nickName : String) = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Gray800)) {
                append("안녕하세요\n")
            }
            withStyle(style = SpanStyle(color = Colors.Main)) {
                append(nickName)
            }
            withStyle(style = SpanStyle(color = Colors.Gray800)) {
                append("님!")
            }
        }
    }

    object SignupEmail {
        const val TITLE = "로그인에 사용할\n이메일을 입력해주세요."
        const val HINT = "이메일 주소를 입력해주세요."
        const val INVALID = "유효한 이메일 주소가 아닙니다."
        const val DUPLICATE = "동일한 이메일 주소로 가입된 계정이 있습니다.\n기존 계정을 확인해주세요."
        const val CODE_HINT = "인증번호를 입력해주세요."
        const val CODE_INVALID = "인증번호가 일치하지 않습니다."
        const val CODE_VALID = "인증번호가 일치합니다."
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

    object PinLog {
        fun getSelectDateTitle(place: String, prefix: String, suffix: String) = buildAnnotatedString {
            if (prefix.isNotEmpty()) {
                withStyle(style = SpanStyle(color = Colors.Gray800)) { append(prefix) }
            }
            withStyle(style = SpanStyle(color = Colors.Main)) { append("’$place’") }
            withStyle(style = SpanStyle(color = Colors.Gray800)) { append(suffix) }
        }
    }

    object Report {
        const val PINLOG_REPORT = "핀로그 신고"
        const val USER_REPORT = "계정 신고"
        const val COMMENT_REPORT = "댓글 신고"

        fun getReportTitle(title: String) = "$title 신고하기"
        fun getDescription(reportType: ReportType): String {
            return when(reportType) {
                COMMENT,
                USER -> "해당 ${reportType.title}을 신고하는 사유를 선택해 주세요."
                PINLOG -> "해당 ${reportType.title}를 신고하는 사유를 선택해 주세요."
            }
        }

        fun getReportReason(reportType: ReportType): List<String> {
            return when(reportType) {
                COMMENT,
                PINLOG -> listOf(
                    "가학적이거나 유해한 내용입니다.",
                    "허위사실을 포함하는 내용입니다.",
                    "혐오를 조장하는 내용입니다.",
                    "기타"
                )
                USER -> listOf(
                    "다른 사람을 사칭하는 계정입니다.",
                    "유해한 내용을 포함하는 계정입니다.",
                    "기타"
                )
            }
        }

        fun getBlockUserDialogTitle(name: String) = "${name}님을 차단하시겠습니까?"
        const val BLOCK_USER_DIALOG_CONTENT = "계정 차단 시, 더 이상 해당 사용자의 핀로그와 댓글이 보이지 않으며 상호 작용할 수 없습니다."

        const val REPORT_SUCCESS_DIALOG_TITLE = "신고가 완료 되었습니다."
        const val REPORT_SUCCESS_DIALOG_CONTENT = "신고 접수된 핀로그는 24시간 이내에 검토되며, 지속적인 신고를 받은 계정은 계정 삭제 처리될 수 있습니다."
        const val UNDO_BLOCK = "차단 해제하기"
        const val UNDO_BLOCK_DIALOG_TITLE = "차단을 해제하시겠습니까?"
        const val UNDO_BLOCK_DIALOG_CONTENT = "차단 해제 시, 해당 계정에 친구 추가, 댓글 등의 상호작용이 가능합니다."
    }

    object Block {
        const val USER_BLOCK = "계정 차단"
    }

    object Toast {
        const val DELETE_PINLOG = "핀로그가 삭제되었어요."
        const val DELETE_PIN_BUDDY = "해당 핀버디가 삭제되었어요"
        const val ACCEPT_PIN_BUDDY = "핀버디 신청을 수락했어요"
        const val REFUSE_PIN_BUDDY = "핀버디 신청을 거절했어요"
        const val CANCEL_PIN_BUDDY_REQEUST = "핀버디 신청을 취소했어요"
        const val EMPTY_PINLOG = "핀로그가 작성되지 않은 장소입니다."
        fun blockSuccessToast(name: String) = "${name}님이 차단되었습니다."
        const val ALREADY_BLOCK_USER = "이미 차단된 유저입니다."
    }

    object Kakao {
        fun getProfileShareTitle(name: String) = "$name 님의 핀업 계정"
        fun getProfileShareContent(name: String) = "핀업에서 $name 님의 핀들을 구경해 보세요."
        const val PROFILE_SHARE_BUTTON = "핀업으로 이동하기"
    }

    object Notification {
        const val ALL_READ = "모두 읽음"
        fun getUnRead(count: Int) = "읽음 $count"
    }

    object Update {
        const val CHECK_UPDATE_TITLE = "업데이트 알림"
        const val CHECK_UPDATE_DESCRIPTION = "더 나은 서비스를 위해 핀업이 업데이트되었어요! 지금 업데이트하고 더 편리하게 사용해주세요"
        const val CHECK_UPDATE_CONFIRM = "업데이트"
        const val CHECK_UPDATE_OPTIONAL_CANCEL = "다음에 하기"
    }
}
