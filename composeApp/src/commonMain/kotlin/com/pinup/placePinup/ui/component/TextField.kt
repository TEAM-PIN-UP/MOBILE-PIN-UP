package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RoundedTextField(
    modifier: Modifier = Modifier,
    textFieldHeight: Int = 47,
    text: String = "",
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    textLimit: Int = Int.MAX_VALUE,
    onFocusChange: (Boolean) -> Unit = {},
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    tailIcon: Painter? = null,
    tailIconPos: Alignment.Vertical = Alignment.CenterVertically,
    isTrailIconAlwaysShow: Boolean = false,
    leadingIcon: Painter? = null,
    tailIconSize: Int = 24,
    onLeadingIconClick: () -> Unit = {},
    onTailIconClick: () -> Unit = {},
    focusedBorderColor: Color = Color.Blue,
    unfocusedBorderColor: Color = Colors.Neutral300,
    isError: Boolean = false,
    fixedBorderColor: Color? = null,
    backgroundColor: Color = Color.White,
    errorBorderColor: Color = Color.Black,
    cornerRounded: Int = 8,
    textStyle: TextStyle = Typography.B2.copy( fontWeight = FontWeight.Medium ),
    placeholderStyle: TextStyle = Typography.B2.copy(fontWeight = FontWeight.Medium),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholderTextColor: Color = Colors.Gray300,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text,
    ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    focusRequester: FocusRequester = FocusRequester(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    cursorColor: Color = Color.Black,
    textColor: Color = Color.Black,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val textSelectionColors = TextSelectionColors(
        handleColor = cursorColor,
        backgroundColor = cursorColor.copy(alpha = 0.4f)
    )
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    val textFieldColor = if (isError) {
        Colors.Negative
    } else if(text.isNotEmpty()){
        Colors.Gray800
    } else{
        Colors.Gray300
    }

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = fixedBorderColor ?: textFieldColor,
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
                .align(Alignment.Center)
                .focusRequester(focusRequester),
            value = text,
            onValueChange = {
                if (it.length <= textLimit) {
                    onValueChange.invoke(it)
                }
            },
            cursorBrush = SolidColor(cursorColor),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(
                color = textFieldColor
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = text,
                    visualTransformation = visualTransformation,
                    innerTextField = @Composable {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
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

                            Box(Modifier.weight(1f)) {
                                innerTextField()
                            }

                            if (isTrailIconAlwaysShow) {
                                Spacer(modifier = Modifier.width(12.dp))

                                if (text.isNotEmpty() && tailIcon != null) {
                                    Image(
                                        modifier = Modifier
                                            .size(tailIconSize.dp)
                                            .align(tailIconPos)
                                            .clickableSingleWithNoRipple {
                                                onTailIconClick.invoke()
                                            },
                                        painter = tailIcon,
                                        contentDescription = null
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(tailIconSize.dp)
                                            .align(tailIconPos)
                                            .clickableSingleWithNoRipple {
                                                onTailIconClick.invoke()
                                            },
                                    )
                                }
                            } else if (tailIcon != null &&
                                text.isNotEmpty() &&
                                isFocused &&
                                readOnly.not()
                            ) {
                                Spacer(modifier = Modifier.width(12.dp))

                                Image(
                                    modifier = Modifier
                                        .size(tailIconSize.dp)
                                        .align(tailIconPos)
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
                                style = placeholderStyle,
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
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = backgroundColor,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IndexedRoundedTextField(
    modifier: Modifier = Modifier,
    index: Int = 0,
    textFieldHeight: Int = 47,
    text: String = "",
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    textLimit: Int = Int.MAX_VALUE,
    onFocusChange: (Boolean) -> Unit = {},
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: Painter? = null,
    isError: Boolean = false,
    fixedBorderColor: Color? = null,
    backgroundColor: Color = Color.White,
    cornerRounded: Int = 8,
    textStyle: TextStyle = Typography.B2.copy( fontWeight = FontWeight.Medium ),
    placeholderStyle: TextStyle = Typography.B2.copy(fontWeight = FontWeight.Medium),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholderTextColor: Color = Colors.Gray300,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text,
    ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    focusRequester: FocusRequester = FocusRequester(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    cursorColor: Color = Color.Black,
    textColor: Color = Color.Black,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val textSelectionColors = TextSelectionColors(
        handleColor = cursorColor,
        backgroundColor = cursorColor.copy(alpha = 0.4f)
    )
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    val textFieldColor = if (isError) {
        Colors.Negative
    } else if(text.isNotEmpty()){
        Colors.Gray800
    } else{
        Colors.Gray300
    }

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = fixedBorderColor ?: textFieldColor,
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
                .align(Alignment.Center)
                .focusRequester(focusRequester),
            value = text,
            onValueChange = {
                if (it.length <= textLimit) {
                    onValueChange.invoke(it)
                }
            },
            cursorBrush = SolidColor(cursorColor),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(
                color = textFieldColor
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = text,
                    visualTransformation = visualTransformation,
                    innerTextField = @Composable {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (text.isNotEmpty()) {
                                Text(
                                    text = "${index + 1}. ",
                                    style = textStyle,
                                    color = Colors.Gray500
                                )
                            }

                            Box(Modifier.weight(1f)) {
                                innerTextField()
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
                                style = placeholderStyle,
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
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = backgroundColor,
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