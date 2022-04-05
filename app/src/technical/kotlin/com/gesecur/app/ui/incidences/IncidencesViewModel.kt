package com.gesecur.app.ui.incidences

import androidx.lifecycle.LiveData
import arrow.core.Either
import arrow.core.getOrElse
import com.gesecur.app.domain.models.Incidence
import com.gesecur.app.domain.models.IncidenceType
import com.gesecur.app.domain.models.User
import com.gesecur.app.domain.repositories.incidence.IncidenceRepository
import com.gesecur.app.domain.repositories.user.UserRepository
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.arch.SingleLiveEvent
import com.gesecur.app.ui.common.arch.State
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.vigilant.VigilantViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

class IncidencesViewModel(
        private val userRepository: UserRepository,
        private val incidenceRepository: IncidenceRepository
) : BaseViewModel() {

    sealed class MainAction: BaseAction() {
        object navigateToAddIncidence: MainAction()
    }

    sealed class Action: BaseAction() {
        object onIncidenceAdded: Action()
        class onLocationChanged(val lat: Double, val lon: Double): Action()
    }

    init {
        getUserIncidenceTypes()
    }

    protected var currentUser: User? = userRepository.getUser().getOrElse { null }

    protected val _mainViewAction: SingleLiveEvent<MainAction> = SingleLiveEvent()
    val mainViewAction: LiveData<MainAction>
        get() = _mainViewAction

    protected val _incidences: SingleLiveEvent<List<Incidence>> = SingleLiveEvent()
    val incidences: LiveData<List<Incidence>>
        get() = _incidences

    protected val _incidenceTypes: SingleLiveEvent<List<IncidenceType>> = SingleLiveEvent()
    val incidenceTypes: LiveData<List<IncidenceType>>
        get() = _incidenceTypes

    fun getUserIncidences(date: LocalDate) = launch {
        _viewState.value = State.Loading

        when (val result = incidenceRepository.getIncidences(currentUser!!.personalId)) {
            is Either.Right -> {
                with(result.b/*.filter { it.dateCreation?.toLocalDate() == date }*/) {
                    _incidences.value = this
                    _viewState.value = if(this.isEmpty()) State.Empty else State.Success
                }
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getUserIncidenceTypes() = launch {
        _viewState.value = State.Loading

        when (val result = incidenceRepository.getIncidenceTypes(currentUser!!.id)) {
            is Either.Right -> {
                _incidenceTypes.value = result.b
                _viewState.value = State.Success
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addIncidence(incidence: Incidence, file: File?) = launch {
        _viewState.value = State.Loading

        when (val result = incidenceRepository.addIncidence(currentUser!!.personalId, currentUser!!.id, incidence, file)) {
            is Either.Right -> {
                _viewAction.value = Action.onIncidenceAdded
                _viewState.value = State.Success
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun changeIncidenceLocation(lat: Double, lon: Double) {
        _viewAction.value = Action.onLocationChanged(lat, lon)
    }

    fun goToAddIncidence() {
        _mainViewAction.value = MainAction.navigateToAddIncidence
    }
}