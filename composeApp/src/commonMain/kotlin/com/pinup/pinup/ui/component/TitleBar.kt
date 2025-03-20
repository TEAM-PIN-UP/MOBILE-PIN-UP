package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pinup.pinup.R
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun TitleBar(
    title: String,
    modifier: Modifier = Modifier,
    onLeftButtonClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
    ) {
        Image(
            modifier = Modifier
                .padding(start = 12.dp)
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