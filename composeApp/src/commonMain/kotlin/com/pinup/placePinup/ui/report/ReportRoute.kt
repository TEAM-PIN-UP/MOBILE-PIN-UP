package com.pinup.placePinup.ui.report

import androidx.compose.runtime.Composable
import com.pinup.placePinup.domain.model.ReportType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportRoute(
    reportType: ReportType,
    onBackPressed: () -> Unit = {},
    viewModel: ReportViewModel = koinViewModel()
) {
    ReportScreen(
        reportType = reportType,
        onBackPressed = onBackPressed,
        onReportClick = {}
    )

    //    if (isShowDeleteDialog) {
//        PDialog(
//            titleText = Texts.PROFILE.REMOVE_PIN_BUDDY_DIALOG_TITLE,
//            descriptionText = Texts.PROFILE.REMOVE_PIN_BUDDY_DIALOG_DESCRIPTION,
//            leftButtonText = Texts.Word.DO_RETURN,
//            rightButtonText = Texts.Word.DO_DELETE,
//            onLeftButtonClick = {
//                isShowDeleteDialog = false
//            },
//            onRightButtonClick = {
//                onRemovePinBuddy()
//                isShowDeleteDialog = false
//            },
//        )
//    }
}
