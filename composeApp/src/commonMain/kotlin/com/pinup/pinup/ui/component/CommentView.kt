package com.pinup.pinup.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Comment
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import com.pinup.pinup.util.relativeOrDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentView(
    modifier: Modifier = Modifier,
    comment: Comment = Comment(),
    onClickMenu: (Int, String) -> Unit = {_, _ -> },
    onReplyClick: (Int) -> Unit = {},
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
                    text = relativeOrDate(comment.createdAt),
                    style = Typography.L2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Gray500
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { if(comment.isOwn) onClickMenu(comment.id, comment.content) },
                    ),
                text = comment.content,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray700
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onReplyClick(comment.id)
                    },
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
                                    text = relativeOrDate(it.createdAt),
                                    style = Typography.L2.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Colors.Gray500
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {},
                                        onLongClick = { if(comment.isOwn) onClickMenu(comment.id, comment.content) },
                                    ),
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