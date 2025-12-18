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

    fun report(reason: String) {
        when(reportType) {
            COMMENT -> reportComment(reason)
            PINLOG -> reportPinlog(reason)
            USER -> reportUser(reason)
        }
    }

    private fun reportUser(reason: String) = viewModelScope.launch {
        val request = ReportUserRequest(
            reportedUserId = targetId,
            reason = reason
        )

        resultResponse(
            response = postReportUserUseCase(request),
            successCallback = { emitEvent(ReportUiEvent.SuccessReport) }
        )
    }

    private fun reportComment(reason: String) = viewModelScope.launch {
        val request = ReportCommentRequest(
            commentId = targetId,
            reason = reason
        )

        resultResponse(
            response = postReportCommentUseCase(request),
            successCallback = { emitEvent(ReportUiEvent.SuccessReport) }
        )
    }

    private fun reportPinlog(reason: String) = viewModelScope.launch {
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