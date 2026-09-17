package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.ui.main.compose.MainDestination
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*

@Composable
fun NotDevelopScreen(
    hasBackButton: Boolean = true,
    onBackPressed: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .background(Colors.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (hasBackButton) {
            TitleBar(
                modifier = Modifier
                    .padding(start = 20.dp),
                onLeftButtonClick = onBackPressed,
            )
        }

        NotDevelopContent(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

/**
 * 하단 탭에 걸려 있는 화면을 준비중으로 대체할 때 쓴다.
 * 탭 자체는 남겨야 하므로 준비중 안내 위에 [BottomBar] 를 그대로 얹는다.
 */
@Composable
fun NotDevelopTabScreen(
    selectedMenu: MainDestination,
    profileImage: String,
    onBottomMenuClick: (MainDestination) -> Unit,
    title: String = "",
) {
    Column(
        modifier = Modifier
            .background(Colors.White)
            .fillMaxSize()
    ) {
        if (title.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = Typography.T1.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray800
                )
            }

            PHorizontalDivider()
        }

        NotDevelopContent(
            modifier = Modifier
                .fillMaxSize()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        BottomBar(
            selectedMenu = selectedMenu,
            profileImage = profileImage,
            onBottomMenuClick = onBottomMenuClick,
        )
    }
}

@Composable
private fun NotDevelopContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_now_develop),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Comming Soon!",
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = stringResource(Res.string.screen_preparing),
            style = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray500,
            textAlign = TextAlign.Center
        )
    }
}
