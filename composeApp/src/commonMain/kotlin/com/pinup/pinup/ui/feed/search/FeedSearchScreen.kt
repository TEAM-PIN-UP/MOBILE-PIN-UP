package com.pinup.pinup.ui.feed.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_back
import pinup.composeapp.generated.resources.ic_close
import pinup.composeapp.generated.resources.ic_search

@Composable
fun FeedSearchScreen(
    query: String,
    profile: String,
    onValueChange: (String) -> Unit,
    onClickBack: () -> Unit = {},
    onClickSearch: () -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
) {

    Column(
        modifier = Modifier
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickBack()
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
                fixedBorderColor = Colors.Gray300,
                backgroundColor = Colors.White,
                contentPadding = PaddingValues(12.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickSearch()
                    },
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = null
            )
        }

        PHorizontalDivider()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ){
        BottomBar(
            selectedMenu = MainDestination.Feed,
            profileImage = profile,
            onBottomMenuClick = onClickBottomNav
        )
    }
}