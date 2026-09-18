package com.pinup.placePinup.ui.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.report.ReportCommentRequest
import com.pinup.placePinup.data.request.report.ReportPinlogRequest
import com.pinup.placePinup.data.request.report.ReportUserRequest
import com.pinup.placePinup.domain.model.ReportType
import com.pinup.placePinup.domain.model.ReportType.*
import com.pinup.placePinup.domain.usecase.PostReportCommentUseCase
import com.pinup.placePinup.domain.usecase.PostReportPinlogUseCase
import com.pinup.placePinup.domain.usecase.PostReportUserUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import kotlinx.coroutines.launch


class ReportViewModel (
    savedStateHandle: SavedStateHandle,
    private val postReportUserUseCase: PostReportUserUseCase,
    private val postReportCommentUseCase: PostReportCommentUseCase,
    private val postReportPinlogUseCase: PostReportPinlogUseCase
) : BaseViewModel<ReportUiState, ReportUiEvent>(ReportUiState()) {
    val targetId = savedStateHandle.get<Int>(TARGET_ID) ?: -1
    val reportType = ReportType.valueOf(savedStateHandle.get<String>(REPORT_TYPE) ?: "")

    init {
        updateState {
            copy(
                reportType = this@ReportViewModel.reportType
            )
        }
    }

    fun report(reason: String) = viewModelScope.launch {
        // 사유 항목은 누르자마자 신고가 나가므로 응답 전에 다시 누르면 신고가 두 번 저장된다. 요청 중엔 막는다.
        if (isLoading.value) return@launch
        withLoading {
            when(reportType) {
                COMMENT -> reportComment(reason)
                PINLOG -> reportPinlog(reason)
                USER -> reportUser(reason)
            }
        }
    }

    private suspend fun reportUser(reason: String) {
        val request = ReportUserRequest(
            reportedUserId = targetId,
            reason = reason
        )

        resultResponse(
            response = postReportUserUseCase(request),
            successCallback = { emitEvent(ReportUiEvent.SuccessReport) }
        )
    }

    private suspend fun reportComment(reason: String) {
        val request = ReportCommentRequest(
            commentId = targetId,
            reason = reason
        )

        resultResponse(
            response = postReportCommentUseCase(request),
            successCallback = { emitEvent(ReportUiEvent.SuccessReport) }
        )
    }

    private suspend fun reportPinlog(reason: String) {
        val request = ReportPinlogRequest(
            pintsId = targetId,
            reason = reason
        )

        resultResponse(
            response = postReportPinlogUseCase(request),
            successCallback = { emitEvent(ReportUiEvent.SuccessReport) }
        )
    }

    companion object {
        private const val TARGET_ID = "targetId"
        private const val REPORT_TYPE = "reportType"

    }
}

data class ReportUiState(
    val reportType: ReportType = ReportType.USER
) : UiState

sealed interface ReportUiEvent : UiEvent {
    data object SuccessReport : ReportUiEvent
}