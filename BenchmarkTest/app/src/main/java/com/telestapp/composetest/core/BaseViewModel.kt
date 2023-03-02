package com.telestapp.composetest.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State, Event>(
    initState: State
) : ViewModel() {

    private val _viewStates: MutableStateFlow<State> = MutableStateFlow(initState)

    protected val scopeIO = CoroutineScope(Dispatchers.IO)

    fun viewStates(): StateFlow<State> = _viewStates

    abstract fun obtainEvent(viewEvent: Event)

    protected fun update(update: (State) -> State) {
        _viewStates.update(update)
    }

}