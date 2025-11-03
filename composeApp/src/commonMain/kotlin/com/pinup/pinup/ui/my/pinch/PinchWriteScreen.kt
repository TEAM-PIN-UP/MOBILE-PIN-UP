package com.pinup.pinup.ui.my.pinch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.extentions.longPressDrag
import com.pinup.pinup.platform.PinchNaverMap
import com.pinup.pinup.ui.component.IndexedRoundedTextField
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import com.pinup.pinup.util.dragModifier
import com.pinup.pinup.util.rememberDragAndDropListState
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_map_off

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PinchWriteScreen(
    title: String = "",
    createdAt: String = "",
    description: String = "",
    pinchList: List<Place> = emptyList(),
    searchedList: List<Place> = emptyList(),
    position: Position = Position.INVALID,
    cameraPosition: Position? = null,
    onBackPressed: () -> Unit = {},
    onTitleChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
    onNameChanged: (Int, String) -> Unit = { _, _ -> },
    onClickDelete: (Int) -> Unit = {},
    moveItem: (Int, Int) -> Unit = { _, _ -> },
    onPlaceClick: (Place, Int) -> Unit = { _, _ -> },
    onMoveWriteReview: (Place) -> Unit = {},
    registerPints: () -> Unit = {},
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val lazyListState = rememberLazyListState()
    val dragAndDropListState =
        rememberDragAndDropListState(lazyListState) { from, to ->
            moveItem(from, to)
        }

    var expandedIndex by remember { mutableStateOf<Int?>(null) }
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val isShowWritePinlogDialog = remember { mutableStateOf(false) }
    val clickedKakaoPlace = remember { mutableStateOf(Place()) }

    Column(
        modifier = Modifier
            .background(
                color = Colors.White
            )
            .statusBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        TitleBar(
            title = Texts.Pinch.PINCH_WRITE,
            onLeftButtonClick = onBackPressed,
        )

        Spacer(modifier = Modifier.height(19.dp))

        Row(
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
                            .clickableWithNoRipple {
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

        Spacer(modifier = Modifier.height(18.dp))

        PinchNaverMap(
            modifier = Modifier,
            position = position,
            placeList = pinchList,
            cameraPosition = cameraPosition,
            onCameraStateChange = {}
        )

        Spacer(modifier = Modifier.height(35.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(Res.drawable.ic_map_off),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(9.dp))

            Text(
                text = "$title 장소목록",
                style = Typography.T1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Black
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            state = dragAndDropListState.lazyListState,
            userScrollEnabled = dragAndDropListState.currentIndexOfDraggedItem == null,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomBarHeight)
                .longPressDrag(
                    onStart = { offset ->
                        dragAndDropListState.onDragStart(offset)
                    },
                    onDrag = { delta ->
                        dragAndDropListState.onDrag(delta)
                        focusManager.clearFocus()
                    },
                    onEnd = {
                        dragAndDropListState.onDragInterrupted()
                        focusManager.clearFocus()
                    },
                ),
        ) {
            itemsIndexed(
                items = pinchList,
            ) { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expandedIndex == index,
                        onExpandedChange = {
                            expandedIndex = if (expandedIndex == index) null else index
                        },
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .dragModifier(index, dragAndDropListState)
                                    .onGloballyPositioned { coordinates ->
                                        textFieldSize = coordinates.size
                                    }
                            ) {
                                IndexedRoundedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = item.name,
                                    onValueChange = {
                                        onNameChanged(index, it)
                                    },
                                    index = index,
                                    placeholder = "${index + 1}. 장소명",
                                    placeholderStyle = Typography.B2.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    textStyle = Typography.B2.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    textColor = Colors.Gray800,
                                    placeholderTextColor = Colors.Gray300,
                                    cornerRounded = 8,
                                    fixedBorderColor = Colors.Gray200,
                                    contentPadding = PaddingValues(
                                        vertical = 24.dp,
                                        horizontal = 16.dp
                                    ),
                                    readOnly = dragAndDropListState.currentIndexOfDraggedItem != null
                                )
                            }

                            Spacer(modifier = Modifier.width(9.dp))

                            RoundedBox(
                                modifier = Modifier
                                    .clickableWithNoRipple {
                                        onClickDelete(index)
                                    },
                                cornerRounded = 999,
                                backgroundColor = Colors.Gray900,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 7.dp, horizontal = 17.dp),
                                    text = Texts.Word.DELETE,
                                    color = Colors.White,
                                    style = Typography.L3.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = expandedIndex == index && searchedList.isNotEmpty() && pinchList[index].name.isNotEmpty(),
                            onDismissRequest = { if (expandedIndex == index) expandedIndex = null },
                            modifier = Modifier
                                .widthIn(
                                    min = with(LocalDensity.current) { textFieldSize.width.toDp() },
                                    max = with(LocalDensity.current) { textFieldSize.width.toDp() }
                                ),
                            properties = PopupProperties(focusable = false)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 226.dp)
                                    .verticalScroll(rememberScrollState())
                            ){
                                searchedList.forEach { place ->
                                    DropdownMenuItem(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {
                                            if (place.hasMyReview) {
                                                onPlaceClick(place, index)
                                            } else {
                                                isShowWritePinlogDialog.value = true
                                                clickedKakaoPlace.value = place
                                            }
                                            expandedIndex = null
                                        }
                                    ) {
                                        Column {
                                            Text(
                                                text = place.name,
                                                color = Colors.Gray800,
                                                style = Typography.L1.copy(
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = place.roadAddress,
                                                color = Colors.Gray600,
                                                style = Typography.L3.copy(
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(5.dp))
                                            PHorizontalDivider()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier
                .background(Colors.White)
                .onGloballyPositioned { coords ->
                    bottomBarHeight = with(density) { coords.size.height.toDp() }
                }
        ) {
            PHorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            PButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = Texts.Pinch.CREATE_PINCH,
                onClick = {
                    registerPints()
                },
                isEnable = pinchList.size > 1
            )

            Spacer(modifier = Modifier.height(39.dp))
        }

    }

    if (isShowWritePinlogDialog.value) {
        PDialog(
            titleText = Texts.Pinch.NO_PINLOG_DIALOG_TITLE,
            descriptionText = Texts.Pinch.NO_PINLOG_DIALOG_CONTENT,
            leftButtonText = Texts.Word.DO_RETURN,
            rightButtonText = Texts.Word.DO_REGISTER,
            onLeftButtonClick = {
                isShowWritePinlogDialog.value = false
            },
            onRightButtonClick = {
                isShowWritePinlogDialog.value = false
                onMoveWriteReview(clickedKakaoPlace.value)
            },
        )
    }
}