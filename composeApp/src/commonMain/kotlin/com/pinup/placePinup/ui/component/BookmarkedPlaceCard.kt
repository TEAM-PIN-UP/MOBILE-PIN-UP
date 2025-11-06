package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.BookmarkedPlace
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography

@Composable
fun BookmarkedPlaceCard(
    bookmarkedPlace: BookmarkedPlace,
    modifier: Modifier = Modifier,
    onUpdateBookmark: (String) -> Unit = { },
) {
    Column(
        modifier = modifier
            .background(
                color = Colors.White
            )
    ) {
        Box {
            ReviewImage(
                modifier = Modifier
                    .size(100.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f),
                imgUrl = bookmarkedPlace.placeFirstReviewImageUrl
            )

            RoundedBox(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .clickableSingleWithNoRipple {
                        onUpdateBookmark(
                            bookmarkedPlace.kakaoPlaceId
                        )
                    },
                backgroundColor = Colors.Neutral50,
                cornerRounded = 100
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(5.dp)
                        .size(16.dp),
                    painter = painterResource(Res.drawable.ic_feed_on),
                    contentDescription = "boomark"
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = when(bookmarkedPlace.placeCategory) {
                    Category.RESTAURANT -> painterResource(Res.drawable.ic_food)
                    else -> painterResource(Res.drawable.ic_cafe)
                },
                contentDescription = "category icon"
            )

            Text(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = bookmarkedPlace.placeName,
                style = Typography.H4,
                color = Colors.Neutral800,
            )
        }
        
        Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = bookmarkedPlace.placeAddress,
            style = Typography.B5,
            color = Colors.Neutral400
        )
    }
}