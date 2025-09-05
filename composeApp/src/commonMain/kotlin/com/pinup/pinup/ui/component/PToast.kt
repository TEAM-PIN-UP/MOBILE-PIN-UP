import androidx.compose.runtime.*
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

class PToastState internal constructor() {
    var message by mutableStateOf<String?>(null)
        private set
    var durationMillis by mutableStateOf(2000L)
        private set

    fun show(text: String, durationMillis: Long = 2000) {
        this.durationMillis = durationMillis
        message = text
    }
    fun dismiss() { message = null }
}

@Composable
fun rememberToastState() = remember { PToastState() }

@Composable
fun PToastHost(
    state: PToastState,
    modifier: Modifier = Modifier,
    content: @Composable (String) -> Unit = { DefaultToast(it) }
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val msg = state.message

        // 메시지가 바뀔 때마다 자동으로 사라지기
        LaunchedEffect(msg, state.durationMillis) {
            if (msg != null) {
                kotlinx.coroutines.delay(state.durationMillis)
                state.dismiss()
            }
        }

        AnimatedVisibility(
            visible = msg != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            msg?.let {
                content(it)
            }
        }
    }
}

@Composable
private fun DefaultToast(text: String) {
    Spacer(modifier = Modifier
        .statusBarsPadding()
        .height(56.dp)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(999.dp),
                color = Color(0xB2000000)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Colors.White,
            style = Typography.B3.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(vertical = 14.dp),
        )
    }
}
