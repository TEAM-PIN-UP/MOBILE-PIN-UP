package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_back

@Composable
fun TitleBar(
    title : String = "",
    modifier: Modifier = Modifier,
    onLeftButtonClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp),
    ) {
        Image(
            modifier = Modifier
                .clickableWithNoRipple { onLeftButtonClick() }
                .align(Alignment.CenterStart),
            painter = painterResource(Res.drawable.ic_back),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            style = Typography.H3,
            color = Colors.Neutral800
        )
    }
}