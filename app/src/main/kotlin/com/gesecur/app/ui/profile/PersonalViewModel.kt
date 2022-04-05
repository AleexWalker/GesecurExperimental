package com.gesecur.app.ui.profile

import androidx.lifecycle.LiveData
import arrow.core.Either
import arrow.core.getOrElse
import com.gesecur.app.domain.models.*
import com.gesecur.app.domain.repositories.personal.PersonalRepository
import com.gesecur.app.domain.repositories.user.UserRepository
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.arch.SingleLiveEvent
import com.gesecur.app.ui.common.arch.State
import com.gesecur.app.ui.common.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File

class PersonalViewModel(
    private val userRepository: UserRepository,
    private val personalRepository: PersonalRepository
) : BaseViewModel() {

    protected var currentUser: User? = userRepository.getUser().getOrElse { null }

    sealed class Action: BaseAction() {
        object ExpenseAdded : Action()
        object MileageAdded : Action()
        object ExpenseDeleted : Action()
        object MileageDeleted : Action()
    }

    protected val _expenses: SingleLiveEvent<List<Expense>> = SingleLiveEvent()
    val expenses: LiveData<List<Expense>>
        get() = _expenses

    protected val _expenseTypes: SingleLiveEvent<List<ExpenseType>> = SingleLiveEvent()
    val expenseTypes: LiveData<List<ExpenseType>>
        get() = _expenseTypes

    protected val _vehicles: SingleLiveEvent<List<Vehicle>> = SingleLiveEvent()
    val vehicles: LiveData<List<Vehicle>>
        get() = _vehicles

    protected val _mileage: SingleLiveEvent<List<Mileage>> = SingleLiveEvent()
    val mileage: LiveData<List<Mileage>>
        get() = _mileage

     fun getCurrentExpenses() = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.getUserExpenses(currentUser!!.id, currentUser!!.personalId)) {
            is Either.Right -> {
                _expenses.value = result.b
                _viewState.value = if(result.b.isEmpty()) State.Empty else State.Success
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getExpenseTypes() = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.getUserExpenseTypes(currentUser!!.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _expenseTypes.value = result.b
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addExpense(expense: Expense, file: File?) = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.addUserExpense(currentUser!!.id, currentUser!!.personalId, expense, file)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.ExpenseAdded
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun deleteExpense(expense: Expense) = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.deleteExpense(currentUser!!.id, expense.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.ExpenseDeleted
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getCurrentMileage() = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.getUserMileage(currentUser!!.id, currentUser!!.personalId)) {
            is Either.Right -> {
                _mileage.value = result.b
                _viewState.value = if(result.b.isEmpty()) State.Empty else State.Success
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getVehicles() = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.getUserVehicles(currentUser!!.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _vehicles.value = result.b
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addMileage(mileage: Mileage, vehicle: Vehicle, file: File?) = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.addUserMileage(currentUser!!.id, currentUser!!.personalId, mileage, vehicle, file)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.MileageAdded
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun deleteMileage(mileage: Mileage) = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.deleteMileage(currentUser!!.id, mileage.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.MileageDeleted
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun closeSession() = launch {
        userRepository.clearAccessToken()
    }
}