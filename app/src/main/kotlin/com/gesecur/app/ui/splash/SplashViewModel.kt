package com.gesecur.app.ui.splash

import arrow.core.Either
import com.gesecur.app.domain.repositories.user.UserRepository
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    val userRepository: UserRepository
) : BaseViewModel() {

    companion object {
        const val SPLASH_DELAY = 2000L
    }

    sealed class Action: BaseAction() {
        object NavigateToOperatorHome : Action()
        object NavigateToVigilantHome : Action()
        object NavigateToAuth : Action()
    }

    fun initSplash() = launch {
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(SPLASH_DELAY)

            checkUserLogged()
        }
    }

    private fun checkUserLogged() = launch {
        when(val result = userRepository.isUserLogged()) {
            is Either.Right -> {
                if (!result.b)
                    _viewAction.value = Action.NavigateToAuth
                else checkUserTypeNavigation()
            }

            is Either.Left -> _viewAction.value = BaseAction.ShowError(result.a)
        }
    }

    private fun checkUserTypeNavigation() = launch {
       when(val result = userRepository.getUser()) {
            is Either.Right -> {
                _viewAction.value =
                    if(result.b.isOperator()) Action.NavigateToOperatorHome
                    else Action.NavigateToVigilantHome
            }
            is Either.Left -> _viewAction.value = Action.NavigateToAuth
        }
    }
}