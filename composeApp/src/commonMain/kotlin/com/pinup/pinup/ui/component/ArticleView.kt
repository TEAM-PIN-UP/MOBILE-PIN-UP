package com.pinup.pinup.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.domain.model.ArticlePlace
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun ArticleView(
    modifier: Modifier = Modifier,
    item: PinchListItem,
    onClickItem: (Int) -> Unit = {},
){
    Column (
        modifier = modifier
            .clickableWithNoRipple {
                onClickItem(item.id)
            }
    ) {
        RoundedBox {
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(335f/188f)
                    .fillMaxWidth(),
                model = item.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = item.title,
            color = Colors.Gray800,
            style = Typography.T1.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(1.dp))

        Text(
            text = item.createdAt,
            color = Colors.Black,
            style = Typography.L1.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}