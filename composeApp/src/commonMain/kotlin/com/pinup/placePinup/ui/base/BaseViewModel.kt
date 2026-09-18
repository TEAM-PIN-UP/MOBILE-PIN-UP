package com.pinup.placePinup.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.util.ScreenState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE: UiState, EVENT : UiEvent>(
    initialPageState : STATE,
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialPageState)
    val uiState: StateFlow<STATE>
        get() = _uiState.asStateFlow()

    private val _uiEvent =  MutableSharedFlow<EVENT>()
    val uiEvent: SharedFlow<EVENT>
        get() = _uiEvent.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    /** 서버 통신 중 로딩 다이얼로그 노출 여부. [withLoading] 으로만 바뀐다. */
    val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    // 겹쳐 호출돼도 마지막 작업이 끝날 때 내리기 위한 카운터.
    // viewModelScope(Main) 안에서만 호출하므로 동기화하지 않는다.
    private var loadingCount = 0

    /**
     * [block] 이 도는 동안 로딩 상태를 켠다.
     * 성공·실패뿐 아니라 예외(응답 파싱 실패 등)로 끝나도 반드시 내려서 다이얼로그가 남지 않게 한다.
     */
    protected suspend fun <T> withLoading(block: suspend () -> T): T {
        loadingCount++
        _isLoading.value = true
        try {
            return block()
        } finally {
            loadingCount--
            if (loadingCount == 0) _isLoading.value = false
        }
    }

    protected fun updateState(
        state: STATE.() -> STATE
    ) {
        _uiState.update { it.state() }
    }

    protected fun emitEvent(event: EVENT) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    protected fun<D> resultResponse(response: PResult<D>, successCallback : (D) -> Unit, errorCallback : ((String) -> Unit)? = null){
        when(response){
            is PResult.Fail -> {
                if (response.failState.code != "E_MEMBER001") ScreenState.updateError(response.failState.message)
                errorCallback?.invoke(response.failState.code)
            }
            is PResult.Success -> {
                successCallback.invoke(response.data)
            }
        }
    }
}