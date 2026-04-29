package com.pinup.placePinup.ui.component.bottomSheet

import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.ReportType
import com.pinup.placePinup.domain.model.ReportType.*
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_block
import pinup.composeapp.generated.resources.ic_report

@Composable
fun ReportBlockMenuBottomSheet(
    reportType: ReportType,
    onClickReport: (ReportType) -> Unit = {},
    onClickBlock: () -> Unit = {},
) {
    val reportText = when(reportType) {
        COMMENT -> stringResource(Res.string.report_type_comment)
        PINLOG -> stringResource(Res.string.report_type_pinlog)
        USER -> stringResource(Res.string.report_type_user)
    }

    Spacer(Modifier.height(40.dp))
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickableWithNoRipple {
                    onClickReport(reportType)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_report),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = reportText,
                color = Colors.Gray800,
                style = Typography.T2.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickableWithNoRipple {
                    onClickBlock()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_block),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(Res.string.block_user_block),
                color = Colors.Gray800,
                style = Typography.T2.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }

    Spacer(Modifier.height(64.dp))
}
