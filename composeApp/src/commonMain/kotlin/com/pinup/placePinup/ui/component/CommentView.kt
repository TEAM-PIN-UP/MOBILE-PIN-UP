package com.pinup.placePinup.ui.component

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
import com.pinup.placePinup.domain.model.AuthorInfo
import com.pinup.placePinup.domain.model.Comment
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import com.pinup.placePinup.util.relativeOrDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommentView(
    modifier: Modifier = Modifier,
    comment: Comment = Comment(),
    onClickMenu: (Boolean, Int, String, AuthorInfo) -> Unit = { _, _, _, _-> },
    onClickProfile: (String) -> Unit = {},
    onReplyClick: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier
    ) {
        ProfileImageView(
            modifier = Modifier
                .clickableWithNoRipple {
                    onClickProfile(comment.author.nickname)
                },
            imgUrl = comment.author.profileImageUrl,
            size = 36.dp,
        )

        Spacer(modifier = Modifier.width(9.dp))

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onClickProfile(comment.author.nickname)
                        },
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
                        onLongClick = {
                            onClickMenu(comment.isOwn, comment.id, comment.content, comment.author)
                        }
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
                            modifier = Modifier
                                .clickableWithNoRipple {
                                    onClickProfile(it.author.nickname)
                                },
                            imgUrl = it.author.profileImageUrl,
                            size = 36.dp,
                        )

                        Spacer(modifier = Modifier.width(9.dp))

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .clickableWithNoRipple {
                                            onClickProfile(it.author.nickname)
                                        },
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
                                        onLongClick = { onClickMenu(comment.isOwn, comment.id, comment.content, comment.author) },
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