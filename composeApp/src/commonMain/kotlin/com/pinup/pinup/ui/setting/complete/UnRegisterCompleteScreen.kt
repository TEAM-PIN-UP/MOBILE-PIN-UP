package com.pinup.pinup.ui.setting.complete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography

@Composable
fun UnRegisterCompleteScreen(
    onMoveOnboarding: () -> Unit = {},
) {

    Column(
        modifier = Modifier
            .background(Colors.White)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp),
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = Texts.Setting.UNREGISTER,
                style = Typography.H3,
                color = Colors.Neutral800
            )
        }

        PHorizontalDivider()

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = Texts.Setting.UNREGISTER_COMPLETE,
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Gray800,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            text = Texts.Word.CONFIRM,
            onClick = {
                onMoveOnboarding()
            },
        )

        Spacer(modifier = Modifier.height(42.dp))
    }
}
