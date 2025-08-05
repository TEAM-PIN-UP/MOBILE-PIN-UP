package com.pinup.pinup.ui.signup.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.signup.TermsOfService
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*


@Composable
fun TermsOfServiceScreen(
    onAllAgreeClick: (TermsOfService) -> Unit,
    onUsingServiceAgreeClick: (TermsOfService) -> Unit,
    onCollectDataAgreeClick: (TermsOfService) -> Unit,
    onCollectLocationAgreeClick: (TermsOfService) -> Unit,
    onMarketingAgreeClick: (TermsOfService) -> Unit,
    onMoveInputName: () -> Unit,
    isAllAgree: Boolean,
    isUsingServiceAgree: Boolean,
    isCollectDataAgree: Boolean,
    isCollectLocationAgree: Boolean,
    isMarketingAgreeClick: Boolean,
    isPassValidation: Boolean,
    onBackPressed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp)
    ) {
        TitleBar(
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        Spacer(modifier = Modifier.height(44.dp))

        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = Texts.SignupTerms.TITLE,
            style = Typography.H0,
            color = Colors.Black
        )

        Spacer(modifier = Modifier.height(40.dp))

        RoundedBox(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithNoRipple {
                    onAllAgreeClick(TermsOfService.ALL)
                },
            cornerRounded = 100,
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 15.dp)
            ) {
                Image(
                    painter = if (isAllAgree) painterResource(Res.drawable.ic_check) else painterResource(Res.drawable.ic_uncheck),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(19.dp))

                Text(
                    text = Texts.SignupTerms.ALL_AGREE,
                    style = Typography.B2,
                    color = Colors.Neutral500
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        PHorizontalDivider()

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(vertical = 15.dp)
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

            Spacer(modifier = Modifier.width(19.dp))

            Text(
                text = Texts.SignupTerms.SERVICE_AGREE,
                style = Typography.B2,
                color = Colors.Neutral500
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(9.dp))
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .padding(vertical = 15.dp)
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

            Spacer(modifier = Modifier.width(19.dp))

            Text(
                text = Texts.SignupTerms.PRIVATE_INFO_AGREE,
                style = Typography.B2,
                color = Colors.Neutral500
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(9.dp))
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .padding(vertical = 15.dp)
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

            Spacer(modifier = Modifier.width(19.dp))

            Text(
                text = Texts.SignupTerms.LOCATION_INFO_AGREE,
                style = Typography.B2,
                color = Colors.Neutral500
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(9.dp))
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .padding(vertical = 15.dp)
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

            Spacer(modifier = Modifier.width(19.dp))

            Text(
                text = Texts.SignupTerms.MARKETING_AGREE,
                style = Typography.B2,
                color = Colors.Neutral500
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(9.dp))
        }

        Spacer(Modifier.weight(1f))

        PButton(
            text = Texts.Word.CONFIRM,
            isEnable = isPassValidation,
            onClick = {
                onMoveInputName()
            }
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}