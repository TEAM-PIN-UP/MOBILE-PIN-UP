package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedTextField(
    modifier: Modifier = Modifier,
    textFieldHeight: Int = 52,
    text: String = "",
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    textLimit: Int = Int.MAX_VALUE,
    onFocusChange: (Boolean) -> Unit = {},
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    tailIcon: Painter? = null,
    isTrailIconAlwaysShow: Boolean = false,
    leadingIcon: Painter? = null,
    tailIconSize: Int = 24,
    onLeadingIconClick: () -> Unit = {},
    onTailIconClick: () -> Unit = {},
    focusedBorderColor: Color = Color.Blue,
    unfocusedBorderColor: Color = Color.Gray,
    backgroundColor: Color = Color.White,
    errorBorderColor: Color = Color.Black,
    cornerRounded: Int = 8,
    textStyle: TextStyle = TextStyle(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholderTextColor: Color = Color.Gray,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text,
    ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    focusRequester: FocusRequester = FocusRequester(),
    contentPadding: PaddingValues = PaddingValues(14.dp),
    cursorColor: Color = Color.Black,
    textColor: Color = Color.Black,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val textSelectionColors = TextSelectionColors(
        handleColor = cursorColor,
        backgroundColor = cursorColor.copy(alpha = 0.4f)
    )
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text)) }
    if (textFieldValue.text != text) {
        textFieldValue = TextFieldValue(text)
    }

    Box(
        modifier = modifier
            .height(textFieldHeight.dp)
            .border(
                width = 1.dp,
                color = if (isFocused) {
                    focusedBorderColor
                } else {
                    unfocusedBorderColor
                },
                shape = RoundedCornerShape(cornerRounded.dp)
            )
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
                onFocusChange.invoke(it.isFocused)
            }
    ) {
        BasicTextField(
            modifier = Modifier
                .align(Alignment.Center),
            value = textFieldValue,
            onValueChange = {
                if (it.text.length <= textLimit) {
                    textFieldValue = it
                    onValueChange.invoke(it.text)
                }
            },
            cursorBrush = SolidColor(cursorColor),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(
                color = textColor
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = textFieldValue.text,
                    visualTransformation = visualTransformation,
                    innerTextField = @Composable {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            if (leadingIcon != null) {
                                Image(
                                    modifier = Modifier
                                        .clickableSingleWithNoRipple {
                                            onLeadingIconClick.invoke()
                                        },
                                    painter = leadingIcon,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(12.dp))
                            }

                            innerTextField.invoke()

                            if (tailIcon != null &&
                                textFieldValue.text.isNotEmpty() &&
                                (isFocused || isTrailIconAlwaysShow) && readOnly.not()
                            ) {
                                Spacer(modifier = Modifier.width(13.dp))

                                Image(
                                    modifier = Modifier
                                        .size(tailIconSize.dp)
                                        .clickableSingleWithNoRipple {
                                            onTailIconClick.invoke()
                                        },
                                    painter = tailIcon,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    placeholder = @Composable {
                        Row {
                            if (leadingIcon != null) {
                                Spacer(modifier = Modifier.width(32.dp))
                            }

                            Text(
                                text = placeholder,
                                style = Typography.B3,
                                color = placeholderTextColor,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    contentPadding = contentPadding,
                    label = null,
                    singleLine = true,
                    enabled = enabled,
                    shape = RoundedCornerShape(cornerRounded.dp),
                    interactionSource = interactionSource,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = backgroundColor,
                        unfocusedContainerColor = backgroundColor,
                        cursorColor = cursorColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )
            }
        )
    }

}