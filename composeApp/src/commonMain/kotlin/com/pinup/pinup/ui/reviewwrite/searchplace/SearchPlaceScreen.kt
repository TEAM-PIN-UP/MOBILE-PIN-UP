package com.pinup.pinup.ui.reviewwrite.searchplace

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.SearchedPlaceCard
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
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
    onBackPressed: () -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.White)
            .padding(horizontal = 20.dp)
    ) {

        TitleBar(
            title = Texts.PinLog.WRITE_PINLOG,
            onLeftButtonClick = onBackPressed
        )

        PHorizontalDivider()


        AnimatedVisibility(
            visible = isFocused.not(),
        ) {
            Column {

                Spacer(modifier = Modifier.height(38.dp))

                Text(
                    text = Texts.PinLog.WRITE_TITLE,
                    color = Colors.Black,
                    style = Typography.D2.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = Texts.PinLog.WRITE_DESCRIPTION,
                    color = Colors.Main,
                    style = Typography.B3.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            text = query,
            textStyle = Typography.T2.copy(
                fontWeight = FontWeight.Medium
            ),
            onValueChange = onValueChange,
            placeholder = Texts.PinLog.SEARCH_HINT,
            placeholderStyle = Typography.T2.copy(
                fontWeight = FontWeight.Medium
            ),
            placeholderTextColor = Colors.Gray400,
            cornerRounded = 100,
            leadingIcon = painterResource(Res.drawable.ic_search),
            tailIcon = if (query.isNotEmpty()) painterResource(Res.drawable.ic_close) else null,
            tailIconSize = 20,
            onTailIconClick = {
                onValueChange("")
            },
            fixedBorderColor = Colors.Transparency,
            backgroundColor = Colors.Gray50,
            onFocusChange = {
                isFocused = it
            },
        )

        Spacer(modifier = Modifier.height(28.dp))

        AnimatedVisibility(
            visible = isFocused,
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = Texts.PinLog.SEARCH_RESULT,
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Gray800
                )

                LazyColumn(
                    modifier = Modifier
                        .padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    items(places) {
                        SearchedPlaceCard(
                            name = it.name,
                            category = Category.of(it.categoryCode),
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