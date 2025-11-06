package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_now_develop

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

        Column(
            modifier = Modifier
                .fillMaxSize(),
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
                text = "아직 준비중인 화면입니다.\n조금만 기다려주세요.",
                style = Typography.B2.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray500,
                textAlign = TextAlign.Center
            )
        }
    }
}