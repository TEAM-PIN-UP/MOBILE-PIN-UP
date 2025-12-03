package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.model.TitleBarButtonType
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_back
import pinup.composeapp.generated.resources.ic_titlebar_close

@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    buttonType: TitleBarButtonType = TitleBarButtonType.BACK,
    title : String = "",
    onLeftButtonClick: () -> Unit = {},
    rightIcon: Painter? = null,
    onRightButtonClick: () -> Unit = {},
) {
    val leftIcon = when (buttonType) {
        TitleBarButtonType.CLOSE -> painterResource(Res.drawable.ic_titlebar_close)
        TitleBarButtonType.BACK -> painterResource(Res.drawable.ic_back)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp),
    ) {
        Image(
            modifier = Modifier
                .clickableWithNoRipple { onLeftButtonClick() }
                .align(Alignment.CenterStart),
            painter = leftIcon,
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray800
        )

        rightIcon?.let {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickableWithNoRipple {
                        onRightButtonClick()
                    },
                painter = it,
                contentDescription = null
            )
        }
    }
}