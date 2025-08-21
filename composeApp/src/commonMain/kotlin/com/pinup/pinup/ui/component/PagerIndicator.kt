package com.pinup.pinup.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.theme.Colors

@Composable
fun PagerIndicator(
    page: Int = 0,
    selectedPage: Int = 0,
) {
    Row {
        for(i in 0 .. page - 1) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(
                        color = if (selectedPage == i) Colors.Gray100 else Colors.Gray300,
                        shape = RoundedCornerShape(999.dp)
                    )
            )

            if (i != page -1) Spacer(modifier = Modifier.width(3.dp))
        }
    }
}