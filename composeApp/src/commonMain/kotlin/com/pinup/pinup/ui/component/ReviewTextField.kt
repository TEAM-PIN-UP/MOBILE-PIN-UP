package com.pinup.pinup.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pinup.pinup.ui.theme.Colors

import com.pinup.pinup.ui.theme.Typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReviewTextField(
    modifier: Modifier = Modifier,
    textFieldHeight: Int = 195,
    text: String = "",
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    textLimit: Int = Int.MAX_VALUE,
    onFocusChange: (Boolean) -> Unit = {},
    readOnly: Boolean = false,
    enabled: Boolean = true,
    backgroundColor: Color = Colors.Gray50,
    cornerRounded: Int = 8,
    textStyle: TextStyle = TextStyle(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholderTextColor: Color = Colors.Gray400,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text,
    ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    focusRequester: FocusRequester = FocusRequester(),
    contentPadding: PaddingValues = PaddingValues(vertical = 24.dp, horizontal = 19.dp),
    cursorColor: Color = Color.Black,
    textColor: Color = Colors.Neutral800,
    maxLength: Int = Int.MAX_VALUE,
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

    RoundedBox(
        modifier = modifier
            .height(textFieldHeight.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
                onFocusChange.invoke(it.isFocused)
            },
        backgroundColor= backgroundColor,
        cornerRounded = cornerRounded
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (textField, counter) = createRefs()
            BasicTextField(
                modifier = Modifier
                    .constrainAs(textField) {
                        linkTo(
                            top = parent.top,
                            bottom = counter.top,
                            bias = 0f
                        )
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.preferredWrapContent
                    },
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
                singleLine = false,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                decorationBox = @Composable { innerTextField ->
                    TextFieldDefaults.TextFieldDecorationBox(
                        value = textFieldValue.text,
                        visualTransformation = visualTransformation,
                        innerTextField = @Composable {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                innerTextField.invoke()
                            }
                        },
                        placeholder = @Composable {
                            Text(
                                modifier = Modifier
                                    .fillMaxSize(),
                                text = placeholder,
                                style = Typography.B3.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = placeholderTextColor,
                            )
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

            Row(
                modifier = Modifier
                    .padding(bottom = 20.dp, end = 20.dp)
                    .constrainAs(counter) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text.length.toString(),
                    style = Typography.B5,
                    color = Colors.Neutral800
                )

                Text(
                    text = " / $maxLength",
                    style = Typography.B5,
                    color = Colors.Neutral300
                )
            }
        }
    }
}