package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_bookmark_off
import pinup.composeapp.generated.resources.ic_bookmark_on
import pinup.composeapp.generated.resources.ic_comment
import pinup.composeapp.generated.resources.ic_heart_off
import pinup.composeapp.generated.resources.ic_heat_on
import pinup.composeapp.generated.resources.ic_map_on
import pinup.composeapp.generated.resources.ic_menu_dot

@Composable
fun FeedView(
    item: Review,
    onClickMenu: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
    onClickDetail: (Int) -> Unit = {},
    onClickScrap: (Int) -> Unit = {},
) {
    var isOverflow by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImageView(
                imgUrl = item.writerProfileImageUrl,
                size = 36.dp,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = item.writerName,
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray900,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.createdAt,
                    style = Typography.L2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Gray500,
                )
            }

            if (item.isOwn) {
                Image(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onClickMenu(item.id)
                        },
                    painter = painterResource(Res.drawable.ic_menu_dot),
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        RoundedBox(
            cornerColor = Colors.Main,
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(12.dp),
                    painter = painterResource(Res.drawable.ic_map_on),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = item.placeName,
                    style = Typography.L2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Main,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!item.reviewImageUrls.isNullOrEmpty()) {
            RoundedBox {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth(),
                    model = item.reviewImageUrls[0],
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = item.content,
            style = Typography.B3.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray700,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                isOverflow = result.lineCount > 2
            }
        )

        if (isOverflow) {
           Text(
               modifier = Modifier
                   .clickableWithNoRipple {
                       onClickDetail(item.id)
                   },
               text = Texts.Word.SEE_MORE,
               style = Typography.B3.copy(
                   fontWeight = FontWeight.Medium
               ),
               color = Colors.Gray400,
           )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickLike(item.id, item.isLikedByUser)
                    },
                painter = painterResource(if (item.isLikedByUser) Res.drawable.ic_heat_on else Res.drawable.ic_heart_off),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(3.dp))

            Text(
                text = item.likeCount.toString(),
                style = Typography.L2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800,
            )

            Spacer(modifier = Modifier.width(14.dp))

            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickDetail(item.id)
                    },
                painter = painterResource(Res.drawable.ic_comment),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = item.commentCount.toString(),
                style = Typography.L2.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800,
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                modifier = Modifier
                    .size(24.dp)
                    .clickableWithNoRipple {
                        onClickScrap(item.id)
                    },
                painter = painterResource(if (item.isScrapByUser) Res.drawable.ic_bookmark_on else Res.drawable.ic_bookmark_off),
                contentDescription = null
            )
        }
    }

}