package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Colors

@Composable
fun BottomBar(
    selectedMenu: MainDestination,
    profileImage: String,
    onBottomMenuClick: (MainDestination) -> Unit
) {
    val iconModifier = Modifier
        .padding(vertical = 9.dp, horizontal = 13.dp)
        .background(Colors.White)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.White)
            .height(56.dp)
    ) {
        PHorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = iconModifier
                    .clickableWithNoRipple {
                        onBottomMenuClick(MainDestination.Map)
                    },
                painter = if (selectedMenu == MainDestination.Map) {
                    painterResource(Res.drawable.ic_map_on)
                } else {
                    painterResource(Res.drawable.ic_map_off)
                },
                contentDescription = "map"
            )

            Image(
                modifier = iconModifier
                    .clickableWithNoRipple {
                        onBottomMenuClick(MainDestination.Bookmark)
                    },
                painter = if (selectedMenu == MainDestination.Bookmark) {
                    painterResource(Res.drawable.ic_bookmark_on)
                } else {
                    painterResource(Res.drawable.ic_bookmark_off)
                },
                contentDescription = "bookmark"
            )

            Image(
                modifier = iconModifier
                    .clickableWithNoRipple {
                        onBottomMenuClick(MainDestination.Upload)
                    },
                painter = if (selectedMenu == MainDestination.Upload) {
                    painterResource(Res.drawable.ic_upload_on)
                } else {
                    painterResource(Res.drawable.ic_upload_off)
                },
                contentDescription = "upload"
            )

            Image(
                modifier = iconModifier
                    .clickableWithNoRipple {
                        onBottomMenuClick(MainDestination.Contents)
                    },
                painter = if (selectedMenu == MainDestination.Contents) {
                    painterResource(Res.drawable.ic_contents_on)
                } else {
                    painterResource(Res.drawable.ic_contents_off)
                },
                contentDescription = "contents"
            )

            if (selectedMenu == MainDestination.My) {
                ProfileImageView(
                    modifier = iconModifier
                        .clickableWithNoRipple {
                            onBottomMenuClick(MainDestination.My)
                        },
                    imgUrl = profileImage,
                    cornerColor = Colors.Neutral800
                )
            } else {
                ProfileImageView(
                    modifier = iconModifier
                        .clickableWithNoRipple {
                            onBottomMenuClick(MainDestination.My)
                        },
                    imgUrl = profileImage,
                )
            }
        }
    }
}
