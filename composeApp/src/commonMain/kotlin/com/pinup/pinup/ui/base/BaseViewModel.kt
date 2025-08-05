package com.pinup.pinup.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PResult
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
    protected val _uiState = MutableStateFlow(initialPageState)
    val uiState: StateFlow<STATE>
        get() = _uiState.asStateFlow()

    private val _uiEvent =  MutableSharedFlow<EVENT>()
    val uiEvent: SharedFlow<EVENT>
        get() = _uiEvent.asSharedFlow()

    protected fun updateState(
        state: STATE.() -> STATE
    ) {
        viewModelScope.launch {
            _uiState.update { it.state() }
        }
    }

    protected fun emitEvent(event: EVENT) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    protected fun<D> resultResponse(response: PResult<D>, successCallback : (D) -> Unit, errorCallback : ((String) -> Unit)? = null){
        when(response){
            is PResult.Fail -> {
                errorCallback?.invoke(response.failState.code)
            }
            is PResult.Success -> {
                successCallback.invoke(response.data)
            }
        }
    }
}