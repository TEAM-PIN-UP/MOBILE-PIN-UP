package com.pinup.placePinup.ui.signup.compose
import pinup.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.platform.openBrowser
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.signup.TermsOfService
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import com.pinup.placePinup.util.Const
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*


@Composable
fun TermsOfServiceScreen(
    onTermAgreeClick: (TermsOfService) -> Unit,
    onMoveInputName: () -> Unit,
    isAllAgree: Boolean,
    isUsingServiceAgree: Boolean,
    isCollectDataAgree: Boolean,
    isCollectLocationAgree: Boolean,
    isMarketingAgreeClick: Boolean,
    isPassValidation: Boolean,
    onBackPressed: () -> Unit,
    onClickDetailTerm: (String) -> Unit
) {
    val context = LocalPlatformContext.current

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
            text = stringResource(Res.string.signup_terms_title),
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Gray800
        )

        Spacer(modifier = Modifier.height(40.dp))

        TermRow(
            type = TermsOfService.ALL,
            onClickCheck = {
                onTermAgreeClick(TermsOfService.ALL)
            },
            isClick = isAllAgree,
        )

        Spacer(modifier = Modifier.height(20.dp))

        PHorizontalDivider()

        Spacer(modifier = Modifier.height(20.dp))

        TermRow(
            type = TermsOfService.USING_SERVICE,
            onClickCheck = {
                onTermAgreeClick(TermsOfService.USING_SERVICE)
            },
            isClick = isUsingServiceAgree,
            onClickDetail = {
                openBrowser(
                    context = context,
                    url = it
                )
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TermRow(
            type = TermsOfService.COLLECT_DATA,
            onClickCheck = {
                onTermAgreeClick(TermsOfService.COLLECT_DATA)
            },
            isClick = isCollectDataAgree,
            onClickDetail = {
                openBrowser(
                    context = context,
                    url = it
                )
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TermRow(
            type = TermsOfService.COLLECT_LOCATION,
            onClickCheck = {
                onTermAgreeClick(TermsOfService.COLLECT_LOCATION)
            },
            isClick = isCollectLocationAgree,
            onClickDetail = {
                openBrowser(
                    context = context,
                    url = it
                )
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TermRow(
            type = TermsOfService.MARKETING,
            onClickCheck = {
                onTermAgreeClick(TermsOfService.MARKETING)
            },
            isClick = isMarketingAgreeClick,
            onClickDetail = {
                openBrowser(
                    context = context,
                    url = it
                )
            }
        )

        Spacer(Modifier.weight(1f))

        PButton(
            text = stringResource(Res.string.word_confirm),
            isEnable = isPassValidation,
            onClick = {
                onMoveInputName()
            }
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}

@Composable
fun TermRow(
    type: TermsOfService,
    onClickCheck: () -> Unit,
    isClick: Boolean,
    onClickDetail: (String) -> Unit = {},
){
    val text = when (type) {
        TermsOfService.ALL -> stringResource(Res.string.signup_terms_all_agree)
        TermsOfService.USING_SERVICE -> stringResource(Res.string.signup_terms_service_agree)
        TermsOfService.COLLECT_DATA -> stringResource(Res.string.signup_terms_private_info_agree)
        TermsOfService.COLLECT_LOCATION -> stringResource(Res.string.signup_terms_location_info_agree)
        TermsOfService.MARKETING -> stringResource(Res.string.signup_terms_marketing_agree)
    }

    val url = when (type) {
        TermsOfService.ALL -> ""
        TermsOfService.USING_SERVICE -> Const.Url.SERVICE_TERM
        TermsOfService.COLLECT_DATA -> Const.Url.PERSONAL_TERM
        TermsOfService.COLLECT_LOCATION -> Const.Url.LOCATION_TERM
        TermsOfService.MARKETING -> Const.Url.MARKETING_TERM
    }

    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .clickableWithNoRipple {
                onClickCheck()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (isClick) painterResource(Res.drawable.ic_check) else painterResource(Res.drawable.ic_uncheck),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(19.dp))

        Text(
            text = text,
            style = Typography.B1.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray500
        )

        Spacer(Modifier.weight(1f))

        if (type != TermsOfService.ALL) {
            Image(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickDetail(url)
                    },
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(9.dp))
        }
    }
}