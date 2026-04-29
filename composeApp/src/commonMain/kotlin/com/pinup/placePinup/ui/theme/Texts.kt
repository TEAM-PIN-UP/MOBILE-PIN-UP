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

    object FindId {
        fun getNickNameTitle(nickName: String, prefix: String, suffix: String) = buildAnnotatedString {
            if (prefix.isNotEmpty()) {
                withStyle(style = SpanStyle(color = Colors.Gray800)) { append(prefix) }
            }
            withStyle(style = SpanStyle(color = Colors.Main)) { append(nickName) }
            withStyle(style = SpanStyle(color = Colors.Gray800)) { append(suffix) }
        }
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

}
