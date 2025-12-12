package com.pinup.placePinup.ui.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.ReportType
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun ReportScreen(
    reportType: ReportType,
    onBackPressed : () -> Unit = {},
    onReportClick : (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .background(Colors.White)
    ) {
        TitleBar(
            onLeftButtonClick = onBackPressed,
            title = Texts.Report.getReportTitle(reportType.name)
        )

        PHorizontalDivider()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = Texts.Report.getDescription(reportType),
            style = Typography.B3.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray400
        )

        Spacer(modifier = Modifier.height(12.dp))

        PHorizontalDivider()

        Texts.Report.getReportReason(reportType).forEach {
            Row(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onReportClick(it)
                    }
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it,
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray800
                )

                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(Res.drawable.ic_right_arrow_300),
                    contentDescription = null
                )
            }

            PHorizontalDivider()
        }


    }
}