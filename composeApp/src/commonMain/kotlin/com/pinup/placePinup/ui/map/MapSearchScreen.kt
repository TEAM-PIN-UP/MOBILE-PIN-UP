package com.pinup.placePinup.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.BottomBar
import com.pinup.placePinup.ui.component.RoundedTextField
import com.pinup.placePinup.ui.main.compose.MainDestination
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*

@Composable
fun MapSearchScreen(
    query: String,
    places: List<Place>,
    profileImage: String,
    onValueChange: (String) -> Unit = {},
    onClickBack: () -> Unit = {},
    onPlaceClick: (String) -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
    showEmptyToast: () -> Unit = {},
) {
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.clickableWithNoRipple { onClickBack() },
                painter = painterResource(Res.drawable.ic_search_back),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            RoundedTextField(
                modifier = Modifier.weight(1f),
                text = query,
                textStyle = Typography.B1.copy(fontWeight = FontWeight.Medium),
                onValueChange = onValueChange,
                placeholder = stringResource(Res.string.pin_map_search_hint),
                placeholderStyle = Typography.T2.copy(fontWeight = FontWeight.Medium),
                placeholderTextColor = Colors.Gray400,
                cornerRounded = 100,
                tailIcon = if (query.isNotEmpty()) painterResource(Res.drawable.ic_close) else null,
                tailIconSize = 20,
                onTailIconClick = { onValueChange("") },
                fixedBorderColor = Colors.Gray900,
                backgroundColor = Colors.White,
                focusRequester = focusRequester,
            )
        }

        FocusScreen(
            places = places,
            onPlaceClick = {
                if (it.reviewCount == 0) showEmptyToast()
                else onPlaceClick(it.kakaoPlaceId)
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        BottomBar(
            selectedMenu = MainDestination.Map,
            profileImage = profileImage,
            onBottomMenuClick = onClickBottomNav,
            onSizeChanged = { bottomBarHeight = it }
        )
    }
}
