package com.pinup.pinup.ui.addpinbuddy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
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
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun AddPinBuddyScreen(
    query: String,
    pinBuddies: PersistentList<PinBuddy>?,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onProfileClick: (Int) -> Unit = {},
    onSearch: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(Colors.White)
            .fillMaxSize()
    ) {
        TitleBar(
            title = "핀버디 추가",
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        PHorizontalDivider()

        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)
                .padding(horizontal = 20.dp),
            text = query,
            onValueChange = onValueChange,
            placeholder = "친구 닉네임을 검색해보세요.",
            cornerRounded = 100,
            leadingIcon = painterResource(Res.drawable.ic_search),
            backgroundColor = Colors.Neutral50,
            unfocusedBorderColor = Colors.Neutral50,
            focusedBorderColor = Colors.Neutral50,
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearch(query)
                }
            ),
        )

        pinBuddies?.let {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = "검색 결과",
                    style = Typography.H4,
                    color = Colors.Neutral800
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
                            .padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(it) {
                            UserCard(
                                imgUrl = it.profile.profilePictureUrl,
                                nickname = it.profile.nickname,
                                reviewCount = it.profile.reviewCount,
                                pinBuddyCount = it.profile.pinBuddyCount,
                                buttonContainer = {
                                    RoundedBox(
                                        modifier = Modifier
                                            .clickableSingleWithNoRipple {
                                                onProfileClick(it.profile.memberId)
                                            },
                                        cornerRounded = 6,
                                        backgroundColor = Colors.Neutral100,
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .padding(vertical = 8.dp, horizontal = 12.dp),
                                            text = "프로필",
                                            color = Colors.Neutral800,
                                            style = Typography.H6
                                        )
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
