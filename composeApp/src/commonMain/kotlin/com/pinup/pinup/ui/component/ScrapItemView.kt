package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_empty_scrap

@Composable
fun ScrapItemView(
    modifier: Modifier = Modifier,
    place: BookmarkedPlace,
) {
    Column(
        modifier = modifier,
    ) {
        if (place.placeFirstReviewImageUrl.isEmpty()) {
            RoundedBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(105f / 90f),
                backgroundColor = Colors.Gray50
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    painter = painterResource( Res.drawable.ic_empty_scrap),
                    contentDescription = null
                )
            }
        } else {
            RoundedBox(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(105f / 90f),
                    model = place.placeFirstReviewImageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = place.placeName,
            color = Colors.Gray500,
            style = Typography.L2.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )
    }
}