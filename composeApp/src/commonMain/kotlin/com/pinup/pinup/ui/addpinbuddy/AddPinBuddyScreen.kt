package com.pinup.pinup.ui.addpinbuddy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.component.UserCard
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Texts
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun AddPinBuddyScreen(
    query: String,
    pinBuddies: PersistentList<PinBuddy>?,
    recentSearchList: List<String>,
    onClickDeleteRecentSearch: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onProfileClick: (Int) -> Unit = {},
    onSearch: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .background(Colors.White)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(vertical = 8.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onBackPressed()
                    },
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            RoundedTextField(
                modifier = Modifier
                    .weight(1f),
                text = query,
                textStyle = Typography.B1.copy(
                    fontWeight = FontWeight.Medium
                ),
                textColor = Colors.Gray800,
                singleLine = true,
                onValueChange = onValueChange,
                cornerRounded = 999,
                tailIcon = if (query.isNotEmpty()) painterResource(Res.drawable.ic_close) else null,
                tailIconSize = 20,
                onTailIconClick = {
                    onValueChange("")
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                        keyboardController?.hide()
                    },
                ),
                fixedBorderColor = Colors.Gray300,
                backgroundColor = Colors.White,
                contentPadding = PaddingValues(12.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onSearch()
                    },
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = null
            )
        }

        PHorizontalDivider()

        if (query.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = Texts.FEED.RECENT_SEARCH,
                    color = Colors.Gray800,
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                recentSearchList.forEachIndexed { index, text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .clickableWithNoRipple {
                                    onValueChange(text)
                                    onSearch()
                                },
                            text = text,
                            color = Colors.Gray700,
                            style = Typography.B1.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            modifier = Modifier
                                .clickableWithNoRipple {
                                    onClickDeleteRecentSearch(index)
                                },
                            painter = painterResource(Res.drawable.ic_cancel_300),
                            contentDescription = null,
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        } else {
            pinBuddies?.let {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = Texts.PROFILE.SEARCH_RESULT,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray800
                    )

                    if (it.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(top = 150.dp),
                                painter = painterResource(Res.drawable.ic_not_found),
                                contentDescription = "empty"
                            )

                            Text(
                                modifier = Modifier
                                    .padding(top = 12.dp),
                                text = "검색 결과가 없어요",
                                style = Typography.H3,
                                color = Colors.Neutral800
                            )

                            Text(
                                modifier = Modifier
                                    .padding(top = 6.dp),
                                text = "검색어를 확인해주세요.",
                                style = Typography.B3,
                                color = Colors.Neutral500
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(top = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            items(it) {
                                UserCard(
                                    modifier = Modifier
                                        .clickableSingleWithNoRipple {
                                            onProfileClick(it.profile.memberId)
                                        },
                                    imgUrl = it.profile.profilePictureUrl,
                                    nickname = it.profile.nickname,
                                    buttonContainer = {
                                        when (it.relationType) {
                                            RelationType.FRIEND -> FriendButton()
                                            RelationType.PENDING -> AlreadyRequestButton()
                                            else -> {}
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FriendButton() {
    RoundedBox(
        cornerRounded = 8,
        backgroundColor = Colors.White,
        cornerColor = Colors.Gray800
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 14.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_pin_buddy),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = Texts.Word.PIN_BUDDY,
                color = Colors.Gray800,
                style = Typography.L1.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun AlreadyRequestButton() {
    RoundedBox(
        cornerRounded = 8,
        backgroundColor = Colors.Gray100,
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 14.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_clock),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = Texts.PROFILE.ALREADY_REQUEST_PIN_BUDDY,
                color = Colors.Gray800,
                style = Typography.L1.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}