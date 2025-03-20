package com.pinup.pinup.ui.signup.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.signup.TermsOfService
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_check
import pinup.composeapp.generated.resources.ic_chevron_right
import pinup.composeapp.generated.resources.ic_uncheck


@Composable
fun TermsOfServiceScreen(
    onAllAgreeClick: (TermsOfService) -> Unit,
    onUsingServiceAgreeClick: (TermsOfService) -> Unit,
    onCollectDataAgreeClick: (TermsOfService) -> Unit,
    onCollectLocationAgreeClick: (TermsOfService) -> Unit,
    onMarketingAgreeClick: (TermsOfService) -> Unit,
    onMoveComplete: () -> Unit,
    isAllAgree: Boolean,
    isUsingServiceAgree: Boolean,
    isCollectDataAgree: Boolean,
    isCollectLocationAgree: Boolean,
    isMarketingAgreeClick: Boolean,
    isPassValidation: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = "약관동의",
            style = Typography.H1,
            color = Colors.Neutral800
        )

        Text(
            modifier = Modifier
                .padding(top = 12.dp),
            text = "핀업 서비스 이용을 위해서는 약관동의가 필요해요.",
            style = Typography.B3,
            color = Colors.Neutral500
        )

        RoundedBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)
                .clickableWithNoRipple {
                    onAllAgreeClick(TermsOfService.ALL)
                },
            cornerRounded = 100,
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Image(
                    painter = if (isAllAgree) painterResource(Res.drawable.ic_check) else painterResource(Res.drawable.ic_uncheck),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .padding(start = 12.dp),
                    text = "전체 동의",
                    style = Typography.B3,
                    color = Colors.Neutral800
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 12.dp)
                .clickableWithNoRipple {
                    onUsingServiceAgreeClick(TermsOfService.USING_SERVICE)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier,
                painter = if (isUsingServiceAgree) painterResource(Res.drawable.ic_check) else painterResource(Res.drawable.ic_uncheck),
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = "핀업 이용약관 동의(필수)",
                style = Typography.B3,
                color = Colors.Neutral800
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 12.dp)
                .clickableWithNoRipple {
                    onCollectDataAgreeClick(TermsOfService.COLLECT_DATA)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier,
                painter = if (isCollectDataAgree) painterResource(Res.drawable.ic_check) else painterResource(Res.drawable.ic_uncheck),
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = "개인정보 수집 및 이용 동의 (필수)",
                style = Typography.B3,
                color = Colors.Neutral800
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 12.dp)
                .clickableWithNoRipple {
                    onCollectLocationAgreeClick(TermsOfService.COLLECT_LOCATION)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier,
                painter = if (isCollectLocationAgree) painterResource(Res.drawable.ic_check) else painterResource(Res.drawable.ic_uncheck),
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = "위치정보 수집 및 이용 동의 (필수)",
                style = Typography.B3,
                color = Colors.Neutral800
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 12.dp)
                .clickableWithNoRipple {
                    onMarketingAgreeClick(TermsOfService.MARKETING)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier,
                painter = if (isMarketingAgreeClick) painterResource(Res.drawable.ic_check) else painterResource(Res.drawable.ic_uncheck),
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = "마케팅 정보 수신 동의 (선택)",
                style = Typography.B3,
                color = Colors.Neutral800
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null
            )
        }

        Spacer(Modifier.weight(1f))

        PButton(
            modifier = Modifier
                .padding(bottom = 28.dp),
            text = "다음",
            isEnable = isPassValidation,
            onClick = {
                onMoveComplete()
            }
        )
    }
}