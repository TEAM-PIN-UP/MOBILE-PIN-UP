package com.pinup.placePinup.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_onboarding_logo
import pinup.composeapp.generated.resources.splash_image

@Composable
fun OnboardingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Gray800),
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(Res.drawable.splash_image),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(290.dp))

            Image(
                modifier = Modifier
                    .padding(bottom = 18.dp),
                painter = painterResource(Res.drawable.ic_onboarding_logo),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                modifier = Modifier,
                text = Texts.Onboarding.ONBOARDING_TEXT,
                style = Typography.H1
            )
        }
    }
}