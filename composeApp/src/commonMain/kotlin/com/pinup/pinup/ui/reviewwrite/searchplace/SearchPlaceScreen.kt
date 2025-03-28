package com.pinup.pinup.ui.reviewwrite.searchplace

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.SearchedPlaceCard
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchPlaceScreen(
    query: String,
    places: PersistentList<Place>,
    onValueChange: (String) -> Unit = {},
    onPlaceClick: (Place) -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.White)
            .padding(horizontal = 20.dp)

    ) {
        AnimatedVisibility(
            visible = isFocused.not(),
        ) {
            Column {
                Image(
                    modifier = Modifier
                        .padding(top = 40.dp),
                    painter = painterResource(Res.drawable.ic_write_review),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    text = "어떤 장소의 리뷰를",
                    color = Colors.Neutral800,
                    style = Typography.H1
                )

                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = "작성할까요?",
                    color = Colors.Neutral800,
                    style = Typography.H1
                )

                Text(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    text = "*작성 된 리뷰는 핀버디만 볼 수 있어요.",
                    color = Colors.Error,
                    style = Typography.B3
                )
            }
        }
        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            text = query,
            onValueChange = onValueChange,
            placeholder = "리뷰 쓸 장소를 검색해주세요.",
            cornerRounded = 100,
            leadingIcon = painterResource(Res.drawable.ic_search),
            backgroundColor = Colors.Neutral50,
            unfocusedBorderColor = Colors.Neutral50,
            focusedBorderColor = Colors.Neutral50,
            onFocusChange = {
                isFocused = it
            }
        )

        AnimatedVisibility(
            modifier = Modifier
                .padding(top = 12.dp),
            visible = isFocused,
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = "검색 결과",
                    style = Typography.H4,
                    color = Colors.Neutral800
                )

                LazyColumn(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(places) {
                        SearchedPlaceCard(
                            name = it.name,
                            address = it.address,
                            reviewCount = it.reviewCount,
                            onClick = {
                                onPlaceClick(it)
                            }
                        )
                    }
                }
            }
        }
    }
}