package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography

@Composable
fun BottomBar(
    selectedMenu: MainDestination,
    profileImage: String,
    onBottomMenuClick: (MainDestination) -> Unit,
    onSizeChanged: (Dp) -> Unit = {},
) {
    val selectedMode = remember { mutableStateOf(false) }
    val density = LocalDensity.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (selectedMode.value) {
            RoundedBox(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentSize()
                    .shadow(elevation = 12.dp, spotColor = Color(0x0F000000), ambientColor = Color(0x0F000000)),
                cornerRounded = 12,
                cornerColor = Colors.Gray200,
            ) {
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .padding(horizontal = 20.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .clickableWithNoRipple {
                                onBottomMenuClick(MainDestination.Upload(true))
                            }
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(14.dp),
                            painter = painterResource(Res.drawable.ic_add_pinbuddy),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = Texts.PinLog.WRITE_PINLOG,
                            color = Colors.Gray600,
                            style = Typography.B3.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    PHorizontalDivider()

                    Row(
                        modifier = Modifier
                            .clickableWithNoRipple {
                                onBottomMenuClick(MainDestination.Upload(false))
                            }
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(14.dp),
                            painter = painterResource(Res.drawable.ic_map_off),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = Texts.Pinch.CREATE_MY_PINCH,
                            color = Colors.Gray600,
                            style = Typography.B3.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickableSingleWithNoRipple {
                    // 바텀시트 터치 이벤트 막기 위해 넣어 놓음
                }
                .onGloballyPositioned { coords ->
                     onSizeChanged(with(density) { coords.size.height.toDp() })
                }
                .background(Colors.White)
                .padding(bottom = 34.dp)
        ) {
            PHorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                BottomBarMenuItem(
                    selectedMenu = selectedMenu,
                    myMenu = MainDestination.Map,
                    onBottomMenuClick = onBottomMenuClick
                )

                BottomBarMenuItem(
                    selectedMenu = selectedMenu,
                    myMenu = MainDestination.Feed,
                    onBottomMenuClick = onBottomMenuClick
                )


                BottomBarMenuItem(
                    selectedMenu = selectedMenu,
                    myMenu = MainDestination.Upload(),
                    selectedMode = selectedMode.value,
                    onBottomMenuClick = onBottomMenuClick,
                    onUploadClick = {
                        selectedMode.value = !selectedMode.value
                    }
                )


                BottomBarMenuItem(
                    selectedMenu = selectedMenu,
                    myMenu = MainDestination.Article,
                    onBottomMenuClick = onBottomMenuClick
                )

                BottomBarMenuItem(
                    selectedMenu = selectedMenu,
                    myMenu = MainDestination.My,
                    imageUrl = profileImage,
                    onBottomMenuClick = onBottomMenuClick
                )
            }
        }
    }
}

@Composable
fun BottomBarMenuItem(
    selectedMenu: MainDestination,
    selectedMode: Boolean = false,
    myMenu: MainDestination,
    imageUrl: String = "",
    onBottomMenuClick: (MainDestination) -> Unit,
    onUploadClick: () -> Unit = {},
){
    val offRes = when(myMenu){
        MainDestination.Article -> Res.drawable.ic_article_off
        MainDestination.Feed -> Res.drawable.ic_feed_off
        MainDestination.Map -> Res.drawable.ic_map_off
        MainDestination.My -> Res.drawable.ic_map_off
        is MainDestination.Upload -> Res.drawable.ic_upload
    }

    val onRes = when(myMenu){
        MainDestination.Article -> Res.drawable.ic_article_on
        MainDestination.Feed -> Res.drawable.ic_feed_on
        MainDestination.Map -> Res.drawable.ic_map_on
        MainDestination.My -> Res.drawable.ic_map_on
        is MainDestination.Upload -> Res.drawable.ic_upload_on
    }

    val text = when(myMenu){
        MainDestination.Article -> Texts.Word.ARTICLE
        MainDestination.Feed -> Texts.Word.FEED
        MainDestination.Map -> Texts.Word.PIN_MAP
        MainDestination.My -> Texts.Word.MY
        is MainDestination.Upload -> ""
    }

    Column(
        modifier = Modifier
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (myMenu is MainDestination.Upload) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .background(Colors.White)
                    .clickableWithNoRipple {
                        onUploadClick()
                    },
                painter = if (selectedMode) {
                    painterResource(onRes)
                } else {
                    painterResource(offRes)
                },
                contentDescription = "contents"
            )
        }
        else if (myMenu != MainDestination.My) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .background(Colors.White)
                    .clickableWithNoRipple {
                        if (myMenu is MainDestination.Upload) return@clickableWithNoRipple
                        onBottomMenuClick(myMenu)
                    },
                painter = if (selectedMenu == myMenu) {
                    painterResource(onRes)
                } else {
                    painterResource(offRes)
                },
                contentDescription = "contents"
            )
        } else {
            ProfileImageView(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .background(Colors.White)
                    .clickableWithNoRipple {
                        onBottomMenuClick(myMenu)
                    },
                imgUrl = imageUrl,
                cornerColor = if(selectedMenu == myMenu) Colors.Main else Colors.Transparency
            )
        }

        if (myMenu != MainDestination.Upload) {
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = text,
                style = Typography.L1,
                color = if(selectedMenu == myMenu) Colors.Main else Colors.Gray800
            )
        }
    }
}
