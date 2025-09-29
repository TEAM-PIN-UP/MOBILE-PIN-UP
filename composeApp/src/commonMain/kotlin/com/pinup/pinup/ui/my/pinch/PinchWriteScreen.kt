package com.pinup.pinup.ui.my.pinch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography

@Composable
fun PinchWriteScreen(
    title: String = "",
    createdAt: String = "",
    description: String = "",
    onBackPressed: () -> Unit = {},
    onTitleChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
) {

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .background(
                color = Colors.White
            )
            .statusBarsPadding()
            .fillMaxWidth()
    ) {
        TitleBar(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            title = Texts.Pinch.PINCH_WRITE,
            onLeftButtonClick = onBackPressed,
        )

        Spacer(modifier = Modifier.height(19.dp))

        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Box {
                Box(modifier = Modifier.width(IntrinsicSize.Min)) {
                    BasicTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester),
                        value = title,
                        onValueChange = onTitleChanged,
                        textStyle = Typography.H1.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Colors.Black
                        ),
                        singleLine = true,
                    )
                }

                if (title.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .clickableWithNoRipple{
                                focusRequester.requestFocus()
                                keyboard?.show()
                            },
                        text = Texts.Pinch.TITLE_HINT,
                        style = Typography.H1.copy(fontWeight = FontWeight.SemiBold),
                        color = Colors.Gray500,
                        maxLines = 1,
                    )
                }
            }

            Spacer(Modifier.width(6.dp))

            Text(
                text = "작성일자 $createdAt",
                style = Typography.L2.copy(fontWeight = FontWeight.Medium),
                color = Colors.Gray500
            )
        }


        Spacer(modifier = Modifier.height(6.dp))

        RoundedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            text = description,
            onValueChange = onDescriptionChanged,
            placeholder = Texts.Pinch.DESCRIPTION_HINT,
            textStyle = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
            placeholderStyle = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
            fixedBorderColor = Colors.Transparency,
            placeholderTextColor = Colors.Gray500,
            textColor = Colors.Black,
            contentPadding = PaddingValues(0.dp),
        )
    }
}
