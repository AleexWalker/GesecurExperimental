package com.gesecur.app.ui.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.arch.SingleLiveEvent
import com.gesecur.app.ui.common.arch.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {
    private var job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    protected val _viewState: SingleLiveEvent<State> = SingleLiveEvent()
    val viewState: LiveData<State>
        get() = _viewState

    protected val _viewAction: SingleLiveEvent<BaseAction> = SingleLiveEvent()
    val viewAction: LiveData<BaseAction>
        get() = _viewAction
}