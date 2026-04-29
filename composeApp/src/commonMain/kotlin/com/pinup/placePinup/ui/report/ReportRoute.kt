package com.pinup.placePinup.ui.report
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.ui.component.PDialog
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportRoute(
    onBackPressed: () -> Unit = {},
    viewModel: ReportViewModel = koinViewModel()
) {
    val isShowReportDialog = remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                ReportUiEvent.SuccessReport -> { isShowReportDialog.value = true }
            }
        }
    }

    ReportScreen(
        reportType = uiState.reportType,
        onBackPressed = onBackPressed,
        onReportClick = {
            viewModel.report(it)
        }
    )

    if (isShowReportDialog.value) {
        PDialog(
            titleText = stringResource(Res.string.report_success_dialog_title),
            descriptionText = stringResource(Res.string.report_success_dialog_content),
            rightButtonText = stringResource(Res.string.word_do_return),
            onRightButtonClick = {
                onBackPressed()
                isShowReportDialog.value = false
            },
        )
    }
}
