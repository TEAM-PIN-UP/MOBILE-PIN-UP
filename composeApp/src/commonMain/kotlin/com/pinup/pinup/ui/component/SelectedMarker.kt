package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.pinup.pinup.R
import com.pinup.pinup.ui.theme.PinUPTheme

@Composable
fun SelectedMarker(
    imgUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_select_marker),
            contentDescription = "marker"
        )
    }
}

@Composable
@Preview
private fun SelectedMarkerPreview() {
    PinUPTheme {
        SelectedMarker(
            imgUrl = ""
        )
    }
}