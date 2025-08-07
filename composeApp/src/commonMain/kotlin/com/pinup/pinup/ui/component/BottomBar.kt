package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography

@Composable
fun BottomBar(
    selectedMenu: MainDestination,
    profileImage: String,
    onBottomMenuClick: (MainDestination) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickableSingleWithNoRipple {
                // 바텀시트 터치 이벤트 막기 위해 넣어 놓음
            }
            .background(Colors.White)
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
                myMenu = MainDestination.Upload,
                onBottomMenuClick = onBottomMenuClick
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

@Composable
fun BottomBarMenuItem(
    selectedMenu: MainDestination,
    myMenu: MainDestination,
    imageUrl: String = "",
    onBottomMenuClick: (MainDestination) -> Unit
){
    val offRes = when(myMenu){
        MainDestination.Article -> Res.drawable.ic_article_off
        MainDestination.Feed -> Res.drawable.ic_feed_off
        MainDestination.Map -> Res.drawable.ic_map_off
        MainDestination.My -> Res.drawable.ic_map_off
        MainDestination.Upload -> Res.drawable.ic_upload
    }

    val onRes = when(myMenu){
        MainDestination.Article -> Res.drawable.ic_article_on
        MainDestination.Feed -> Res.drawable.ic_feed_on
        MainDestination.Map -> Res.drawable.ic_map_on
        MainDestination.My -> Res.drawable.ic_map_on
        MainDestination.Upload -> Res.drawable.ic_upload
    }

    val text = when(myMenu){
        MainDestination.Article -> Texts.Word.ARTICLE
        MainDestination.Feed -> Texts.Word.FEED
        MainDestination.Map -> Texts.Word.PIN_MAP
        MainDestination.My -> Texts.Word.MY
        MainDestination.Upload -> ""
    }

    Column(
        modifier = Modifier
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (myMenu != MainDestination.My) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .background(Colors.White)
                    .clickableWithNoRipple {
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
