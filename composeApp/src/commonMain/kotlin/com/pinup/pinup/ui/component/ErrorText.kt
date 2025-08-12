package com.pinup.pinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_error_circle
import pinup.composeapp.generated.resources.ic_error_circle_red

@Composable
fun ErrorText(
    text : String,
    isNotError: Boolean = false
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.padding(top = 4.dp),
            painter = painterResource(if(isNotError) Res.drawable.ic_error_circle else Res.drawable.ic_error_circle_red),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = text,
            style = Typography.B5,
            color = if(isNotError) Colors.Gray500 else Colors.Error
        )
    }
}