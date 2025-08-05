package com.pinup.pinup.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_onboarding_logo

@Composable
fun OnboardingScreen(
    onClickStart: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Neutral800),
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

        Spacer(modifier = Modifier.weight(1f))

        RoundedBox(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clickableSingleWithNoRipple {
                    onClickStart()
                },
            cornerRounded = 999,
            backgroundColor = Colors.White,
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .align(Alignment.Center),
                text = Texts.Word.WORD_START,
                style = Typography.H4,
                color = Colors.Neutral800
            )
        }

        Spacer(modifier = Modifier.height(42.dp))


    }
}