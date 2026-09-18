package com.pinup.placePinup.ui.my.pinch.detail
import org.jetbrains.compose.resources.stringResource

import PintsMenuBottomSheet
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.platform.PinchNaverMap
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_map_off
import pinup.composeapp.generated.resources.ic_menu_dot

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PinchDetailScreen(
    title: String = "",
    createdAt: String = "",
    description: String = "",
    pinchList: List<Place> = emptyList(),
    position: Position = Position.INVALID,
    cameraPosition: Position? = null,
    onBackPressed: () -> Unit = {},
    onClickEdit: () -> Unit = {},
    onClickDelete: () -> Unit = {},
    getPintsDetail: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )

    LaunchedEffect(Unit){
        getPintsDetail()
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            PintsMenuBottomSheet(
                onClickEdit = {
                    onClickEdit()
                    scope.launch { sheetState.hide() }
                },
                onClickDelete = {
                    isShowDeleteDialog.value = true
                    scope.launch { sheetState.hide() }
                },
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
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
                title = stringResource(Res.string.pinch_detail),
                onLeftButtonClick = onBackPressed,
                rightIcon = painterResource(Res.drawable.ic_menu_dot),
                onRightButtonClick = {
                    scope.launch { sheetState.show() }
                }
            )

            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(19.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = title,
                            style = Typography.H1.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = Colors.Black,
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            text = stringResource(Res.string.pinch_created_date, createdAt),
                            style = Typography.L2.copy(fontWeight = FontWeight.Medium),
                            color = Colors.Gray500
                        )
                    }


                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = description,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Black
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(18.dp))

                    PinchNaverMap(
                        modifier = Modifier,
                        position = position,
                        placeList = pinchList,
                        cameraPosition = cameraPosition,
                        onCameraStateChange = {}
                    )

                    Spacer(modifier = Modifier.height(35.dp))
                }

                item {
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
                            text = stringResource(Res.string.pinch_place_list),
                            style = Typography.T1.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                itemsIndexed(
                    items = pinchList,
                ) { index, item ->
                    RoundedBox(
                        modifier = Modifier
                            .fillMaxWidth(),
                        cornerColor = Colors.Gray200,
                        cornerRounded = 8
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 24.dp, horizontal = 16.dp),
                            text = if(item.name.isNotEmpty()) "${index + 1}. ${item.name}" else "",
                            style = Typography.B2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray800,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    if (isShowDeleteDialog.value) {
        PDialog(
            titleText = stringResource(Res.string.pinch_delete_dialog_title),
            descriptionText = stringResource(Res.string.pinch_delete_dialog_content),
            leftButtonText = stringResource(Res.string.word_do_return),
            rightButtonText = stringResource(Res.string.word_do_delete),
            onLeftButtonClick = {
                isShowDeleteDialog.value = false
            },
            onRightButtonClick = {
                isShowDeleteDialog.value = false
                onClickDelete()
            },
        )
    }
}
