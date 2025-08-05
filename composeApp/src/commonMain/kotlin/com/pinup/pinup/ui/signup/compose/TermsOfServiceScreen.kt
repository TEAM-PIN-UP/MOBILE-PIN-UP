package com.pinup.pinup.ui.signup.compose

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
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.signup.TermsOfService
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import com.pinup.pinup.util.Const
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
            onClickDetail = onClickDetailTerm
        )

        Spacer(modifier = Modifier.height(5.dp))

        TermRow(
            type = TermsOfService.COLLECT_DATA,
            onClickCheck = {
                onTermAgreeClick(TermsOfService.COLLECT_DATA)
            },
            isClick = isCollectDataAgree,
            onClickDetail = onClickDetailTerm
        )

        Spacer(modifier = Modifier.height(5.dp))

        TermRow(
            type = TermsOfService.COLLECT_LOCATION,
            onClickCheck = {
                onTermAgreeClick(TermsOfService.COLLECT_LOCATION)
            },
            isClick = isCollectLocationAgree,
            onClickDetail = onClickDetailTerm
        )

        Spacer(modifier = Modifier.height(5.dp))

        TermRow(
            type = TermsOfService.MARKETING,
            onClickCheck = {
                onTermAgreeClick(TermsOfService.MARKETING)
            },
            isClick = isMarketingAgreeClick,
            onClickDetail = onClickDetailTerm
        )

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

@Composable
fun TermRow(
    type: TermsOfService,
    onClickCheck: () -> Unit,
    isClick: Boolean,
    onClickDetail: (String) -> Unit = {},
){
    val text = when (type) {
        TermsOfService.ALL -> Texts.SignupTerms.ALL_AGREE
        TermsOfService.USING_SERVICE -> Texts.SignupTerms.SERVICE_AGREE
        TermsOfService.COLLECT_DATA -> Texts.SignupTerms.PRIVATE_INFO_AGREE
        TermsOfService.COLLECT_LOCATION -> Texts.SignupTerms.LOCATION_INFO_AGREE
        TermsOfService.MARKETING -> Texts.SignupTerms.MARKETING_AGREE
    }

    val url = when (type) {
        TermsOfService.ALL -> ""
        TermsOfService.USING_SERVICE -> Const.Url.TERM_SERVICE
        TermsOfService.COLLECT_DATA -> Const.Url.TERM_PRIVATE
        TermsOfService.COLLECT_LOCATION -> Const.Url.TERM_LOCATION
        TermsOfService.MARKETING -> Const.Url.TERM_MARKETING
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
            style = Typography.B2,
            color = Colors.Neutral500
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