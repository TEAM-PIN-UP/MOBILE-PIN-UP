package com.pinup.pinup.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


private val PretendardStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
)

val Typography = PinUpTypography(
    D1 = PretendardStyle.copy(
        fontSize = 30.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.02).sp
    ),
    D2 = PretendardStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.02).sp
    ),
    T1 = PretendardStyle.copy(
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.02).sp
    ),
    T2 = PretendardStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.02).sp
    ),
    H0 = PretendardStyle.copy(
        fontWeight = FontWeight.W600,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.4).sp
    ),
    H1 = PretendardStyle.copy(
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.4).sp
    ),
    H2 = PretendardStyle.copy(
        fontWeight = FontWeight.W600,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.36).sp
    ),
    H3 = PretendardStyle.copy(
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.32).sp
    ),
    H4 = PretendardStyle.copy(
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.28).sp
    ),
    H5 = PretendardStyle.copy(
        fontWeight = FontWeight.W600,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.26).sp
    ),
    H6 = PretendardStyle.copy(
        fontWeight = FontWeight.W600,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.24).sp
    ),
    B1 = PretendardStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.02).sp
    ),
    B2 = PretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.02).sp
    ),
    B3 = PretendardStyle.copy(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.02).sp
    ),
    B4 = PretendardStyle.copy(
        fontWeight = FontWeight.W500,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.26).sp
    ),
    B5 = PretendardStyle.copy(
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.24).sp
    ),
    B6 = PretendardStyle.copy(
        fontWeight = FontWeight.W500,
        fontSize = 11.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.22).sp
    ),
    C1 = PretendardStyle.copy(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.28).sp
    ),
    C2 = PretendardStyle.copy(
        fontWeight = FontWeight.W400,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.26).sp
    ),
    C3 = PretendardStyle.copy(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.24).sp
    ),

    L1 = PretendardStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.02).sp
    ),
    L2 = PretendardStyle.copy(
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = (-0.02).sp
    ),
    L3 = PretendardStyle.copy(
        fontSize = 10.sp,
        lineHeight = 13.sp,
        letterSpacing = (-0.02).sp
    ),
)

@Immutable
data class PinUpTypography(
    val D1: TextStyle,
    val D2: TextStyle,

    val T1: TextStyle,
    val T2: TextStyle,

    val H0: TextStyle,
    val H1: TextStyle,
    val H2: TextStyle,
    val H3: TextStyle,
    val H4: TextStyle,
    val H5: TextStyle,
    val H6: TextStyle,

    val B1: TextStyle,
    val B2: TextStyle,
    val B3: TextStyle,
    val B4: TextStyle,
    val B5: TextStyle,
    val B6: TextStyle,

    val L1: TextStyle,
    val L2: TextStyle,
    val L3: TextStyle,

    val C1: TextStyle,
    val C2: TextStyle,
    val C3: TextStyle,
)