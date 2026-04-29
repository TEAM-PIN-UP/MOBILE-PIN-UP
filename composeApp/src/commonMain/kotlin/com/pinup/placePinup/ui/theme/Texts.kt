package com.pinup.placePinup.ui.theme


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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
        fun getText(prefix: String, highlight: String) = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Gray50)) { append(prefix) }
            withStyle(style = SpanStyle(color = Colors.Main)) { append(highlight) }
        }
        fun getLoginTitle(prefix: String, highlight: String) = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Gray800)) { append(prefix) }
            withStyle(style = SpanStyle(color = Colors.Main)) { append(highlight) }
        }
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

}
