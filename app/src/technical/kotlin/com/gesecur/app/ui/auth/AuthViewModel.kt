package com.gesecur.app.ui.auth

import arrow.core.Either
import com.gesecur.app.R
import com.gesecur.app.domain.models.User
import com.gesecur.app.domain.repositories.user.UserRepository
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.arch.State
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.utils.isValidEmail
import kotlinx.coroutines.launch

class AuthViewModel(
    val userRepository: UserRepository
) : BaseViewModel() {


    sealed class Action: BaseAction() {
        class OnUserLogged(val user: User) : Action()
    }

    fun login(email: String, password: String) = launch {
        if(validateLoginData(email, password)) {
            _viewState.value = State.Loading
            when (val result = userRepository.login(email, password)) {
                is Either.Right -> {
                    _viewState.value = State.Success
                    if(result.b != null)
                        _viewAction.value = Action.OnUserLogged(result.b!!)
                    else
                        _viewAction.value = BaseAction.ShowErrorRes(R.string.ERROR_UNKNOWN)
                }

                is Either.Left -> {
                    _viewState.value = State.Success
                    _viewAction.value = BaseAction.ShowError(result.a)
                }
            }
        }
        else {
            _viewState.value = State.Success
            _viewAction.value = BaseAction.ShowErrorRes(R.string.ERROR_FORM_VALIDATION)
        }
    }

    private fun validateLoginData(email: String, password: String): Boolean {
        return email.isValidEmail() && password.isNotEmpty()
    }
}