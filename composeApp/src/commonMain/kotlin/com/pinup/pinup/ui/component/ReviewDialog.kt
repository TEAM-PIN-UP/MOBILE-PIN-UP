package com.pinup.pinup.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.PinUPTheme
import com.pinup.pinup.ui.theme.Typography

@Composable
fun ReviewDialog(
    rating: Int,
    onDismissRequest: () -> Unit,
    onConfirmClick: (Int) -> Unit = {},
) {
    val selectedRating = remember { mutableIntStateOf(rating) }
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = Color.White)
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "별점 남기기",
                color = Colors.Neutral800,
                style = Typography.H2,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                text = "식당 이용 경험은 어떠셨나요?",
                color = Colors.Neutral500,
                style = Typography.B3,
                textAlign = TextAlign.Center
            )

            HalfStarRatingBar(
                modifier = Modifier
                    .padding(top = 20.dp),
                rating = selectedRating.intValue,
            ) {
                selectedRating.intValue = it
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "취소",
                    color = Colors.Neutral400,
                    style = Typography.H4,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(color = Colors.White)
                        .padding(vertical = 14.dp)
                        .clickableWithNoRipple {
                            onDismissRequest()
                        }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "완료",
                    color = Colors.White,
                    style = Typography.H4,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(100.dp))
                        .background(color = Colors.Neutral800)
                        .padding(vertical = 14.dp)
                        .clickableWithNoRipple {
                            onConfirmClick(selectedRating.intValue)
                        }
                )
            }
        }
    }
}

@Composable
@Preview
private fun ReviewDialogPreview() {
    PinUPTheme {
        ReviewDialog(
            rating = 0,
            onDismissRequest = {}
        )
    }
}