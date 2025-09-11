package com.pinup.pinup.ui.pinlogDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.domain.model.AuthorInfo
import com.pinup.pinup.domain.model.Comment
import com.pinup.pinup.domain.model.PinlogDetail
import com.pinup.pinup.domain.model.ReplyComment
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.CommentMenuBottomSheet
import com.pinup.pinup.ui.component.CommentView
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.PagerIndicator
import com.pinup.pinup.ui.component.PinlogMenuBottomSheet
import com.pinup.pinup.ui.component.ProfileImageView
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Texts.PinLog.COMMENT_REPLY_HINT
import com.pinup.pinup.ui.theme.Typography
import com.pinup.pinup.util.toShortDateXd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_bookmark_off
import pinup.composeapp.generated.resources.ic_bookmark_on
import pinup.composeapp.generated.resources.ic_comment
import pinup.composeapp.generated.resources.ic_comment_upload
import pinup.composeapp.generated.resources.ic_heart_off
import pinup.composeapp.generated.resources.ic_heat_on
import pinup.composeapp.generated.resources.ic_menu_dot
import pinup.composeapp.generated.resources.ic_right_arrow_300
import pinup.composeapp.generated.resources.ic_star

@Composable
fun PinlogDetailScreen(
    pinlogDetail: PinlogDetail,
    query: String,
    userInfo: UserInfo,
    replyId: Int?,
    onValueChange: (String) -> Unit = {},
    onClickPlaceDetail: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onClickEdit: () -> Unit = {},
    onClickDelete: () -> Unit = {},
    onClickUploadComment: () -> Unit = {},
    onClickEditComment: (Int, String) -> Unit = {_, _ -> },
    onClickDeleteComment: (Int) -> Unit = {},
    updateNonFocusMode: () -> Unit = {},
    updateReplyCommentId: (Int) -> Unit = {},
) {

    val density = LocalDensity.current
    var bottomBarHeightPx by remember { mutableStateOf(0) }
    val bottomBarHeightDp = with(receiver = density) { bottomBarHeightPx.toDp() }
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    var commentSheet by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { pinlogDetail.reviewImageUrls.size })
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    var clickedCommentId by remember { mutableStateOf(0) }
    var clickedComment by remember { mutableStateOf("") }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            if (commentSheet) {
                CommentMenuBottomSheet(
                    onClickEdit = {
                        onClickEditComment(clickedCommentId, clickedComment)
                        scope.launch { sheetState.hide() }
                        focusRequester.requestFocus()
                    },
                    onClickDelete = {
                        onClickDeleteComment(clickedCommentId)
                        scope.launch { sheetState.hide() }
                    },
                )
            } else {
                PinlogMenuBottomSheet(
                    onClickEdit = {
                        onClickEdit()
                        scope.launch { sheetState.hide() }
                    },
                    onClickDelete = {
                        onClickDelete()
                        scope.launch { sheetState.hide() }
                    },
                )
            }
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus(force = true)
                            keyboard?.hide()
                            updateNonFocusMode()
                        })
                    }
            ){
                TitleBar(
                    modifier = Modifier
                        .padding(start = 20.dp),
                    title = Texts.PinLog.DETAIL_TITLE,
                    onLeftButtonClick = onBackPressed
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = bottomBarHeightDp + 20.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {

                                Text(
                                    text = pinlogDetail.placeName,
                                    style = Typography.B2.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Colors.Black,
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "${toShortDateXd(pinlogDetail.visitedDate)} 방문",
                                    style = Typography.L2.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Colors.Gray400,
                                )
                            }

                            Image(
                                modifier = Modifier
                                    .clickableWithNoRipple {
                                        onClickPlaceDetail("")
                                    },
                                painter = painterResource(Res.drawable.ic_right_arrow_300),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Colors.Gray400)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        PHorizontalDivider()

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileImageView(
                                imgUrl = pinlogDetail.writerProfileImageUrl,
                                size = 36.dp,
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = pinlogDetail.writerName,
                                    style = Typography.B2.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Colors.Gray900,
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = Texts.Word.PINLOG,
                                        style = Typography.L2.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = Colors.Gray500,
                                    )

                                    Spacer(modifier = Modifier.width(2.dp))

                                    Text(
                                        text = pinlogDetail.authorReviewCount.toString(),
                                        style = Typography.L2.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = Colors.Gray800,
                                    )
                                }
                            }

                            if (pinlogDetail.isOwn) {
                                Image(
                                    modifier = Modifier
                                        .clickableWithNoRipple {
                                            scope.launch { sheetState.show() }
                                        },
                                    painter = painterResource(Res.drawable.ic_menu_dot),
                                    contentDescription = null,
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))

                        Column {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.ic_star),
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(2.dp))

                                Text(
                                    text = pinlogDetail.starRating.toString(),
                                    style = Typography.B2.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Colors.Gray900,
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = "${pinlogDetail.createdAt[0]}.${pinlogDetail.createdAt[1]}.${pinlogDetail.createdAt[2]} 작성",
                                    style = Typography.L2.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Colors.Gray400,
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (pinlogDetail.reviewImageUrls.isNotEmpty()) {
                                Box {
                                    HorizontalPager(
                                        state = pagerState,
                                    ) { page ->
                                        RoundedBox(
                                            modifier = Modifier
                                                .padding(horizontal = 20.dp),
                                            cornerRounded = 8,
                                            backgroundColor = Colors.Gray100,
                                        ) {
                                            AsyncImage(
                                                modifier = Modifier
                                                    .aspectRatio(1f)
                                                    .fillMaxWidth(),
                                                model = pinlogDetail.reviewImageUrls[page],
                                                contentScale = ContentScale.Crop,
                                                contentDescription = "default profile image"
                                            )
                                        }
                                    }

                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                    ) {
                                        PagerIndicator(
                                            page = pagerState.pageCount,
                                            selectedPage = pagerState.currentPage
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp),
                                text = pinlogDetail.content,
                                style = Typography.B3.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Gray700,
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(if (pinlogDetail.isLikedByUser) Res.drawable.ic_heat_on else Res.drawable.ic_heart_off),
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(3.dp))

                                Text(
                                    text = pinlogDetail.likeCount.toString(),
                                    style = Typography.L2.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Colors.Gray800,
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Image(
                                    painter = painterResource(Res.drawable.ic_comment),
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = pinlogDetail.commentCount.toString(),
                                    style = Typography.L2.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Colors.Gray800,
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Image(
                                    modifier = Modifier
                                        .size(24.dp),
                                    painter = painterResource(if (pinlogDetail.isScrapByUser) Res.drawable.ic_bookmark_on else Res.drawable.ic_bookmark_off),
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(50.dp))

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = Texts.Word.COMMENT,
                                style = Typography.T2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Gray800,
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = pinlogDetail.commentCount.toString(),
                                style = Typography.T2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Gray800,
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        PHorizontalDivider()

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(pinlogDetail.comments){
                        CommentView(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            comment = it,
                            onClickMenu = { id, comment ->
                                commentSheet = true
                                clickedCommentId = id
                                clickedComment = comment
                                scope.launch { sheetState.show() }
                            },
                            onReplyClick = { id ->
                                updateReplyCommentId(id)
                                focusRequester.requestFocus()
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .onGloballyPositioned { coords ->
                        bottomBarHeightPx = coords.size.height
                    }
            ) {

                PHorizontalDivider()

                Row(
                    modifier = Modifier
                        .background(Colors.White)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileImageView(
                        imgUrl = userInfo.profileUrl,
                        size = 35.dp
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    RoundedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = query,
                        textStyle = Typography.B3.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        singleLine = false,
                        onValueChange = onValueChange,
                        placeholder = if(replyId == null) Texts.PinLog.COMMENT_HINT else COMMENT_REPLY_HINT,
                        placeholderStyle = Typography.B3.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        placeholderTextColor = Colors.Gray500,
                        cornerRounded = 24,
                        tailIcon = if (query.isNotEmpty()) painterResource(Res.drawable.ic_comment_upload) else null,
                        tailIconSize = 38,
                        onTailIconClick = {
                            onClickUploadComment()
                        },
                        fixedBorderColor = Colors.Gray300,
                        backgroundColor = Colors.White,
                        contentPadding = PaddingValues(12.dp),
                        focusRequester = focusRequester
                    )
                }
            }
        }
    }
}