package com.gesecur.app.ui.common.arch


sealed class State {
    object Success : State()
    object Loading : State()
    object Empty : State()
}