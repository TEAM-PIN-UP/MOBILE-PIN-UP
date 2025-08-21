package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Comment
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_menu_dot

@Composable
fun CommentView(
    modifier: Modifier = Modifier,
    comment: Comment = Comment(),
) {
    Row(
        modifier = modifier
    ) {
        ProfileImageView(
            imgUrl = comment.author.profileImageUrls,
            size = 36.dp,
        )

        Spacer(modifier = Modifier.width(9.dp))

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.author.nickname,
                    style = Typography.L1.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray900
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = comment.createdAt,
                    style = Typography.L2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Gray500
                )

                Spacer(modifier = Modifier.weight(1f))

                if (comment.isOwn) {
                    Image(
                        modifier = Modifier
                            .size(16.dp),
                        painter = painterResource(Res.drawable.ic_menu_dot),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = comment.content,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray700
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = Texts.Word.DO_REPLY_COMMENT,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray500
            )

            if (comment.replies.isNotEmpty()) {

                Spacer(modifier = Modifier.height(24.dp))

                comment.replies.forEach {
                    Row(
                    ) {
                        ProfileImageView(
                            imgUrl = it.author.profileImageUrls,
                            size = 36.dp,
                        )

                        Spacer(modifier = Modifier.width(9.dp))

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = it.author.nickname,
                                    style = Typography.L1.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Colors.Gray900
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = it.createdAt,
                                    style = Typography.L2.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Colors.Gray500
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                if (it.isOwn) {
                                    Image(
                                        modifier = Modifier
                                            .size(16.dp),
                                        painter = painterResource(Res.drawable.ic_menu_dot),
                                        contentDescription = null
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = it.content,
                                style = Typography.B3.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Gray700
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}