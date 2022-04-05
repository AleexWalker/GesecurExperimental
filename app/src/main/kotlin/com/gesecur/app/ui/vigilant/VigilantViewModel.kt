package com.gesecur.app.ui.vigilant

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import arrow.core.Either
import arrow.core.getOrElse
import com.gesecur.app.domain.models.NewsRegistry
import com.gesecur.app.domain.models.Turn
import com.gesecur.app.domain.models.User
import com.gesecur.app.domain.repositories.user.UserRepository
import com.gesecur.app.domain.repositories.vigilant.VigilantRepository
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.arch.SingleLiveEvent
import com.gesecur.app.ui.common.arch.State
import com.gesecur.app.ui.common.base.BaseViewModel
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class VigilantViewModel(
    private val userRepository: UserRepository,
    private val vigilantRepository: VigilantRepository
) : BaseViewModel() {

    protected var currentUser: User? = userRepository.getUser().getOrElse { null }

    sealed class Action: BaseAction() {
        object TurnStarted : Action()
        class TurnStartedSeconds(val seconds: Duration) : Action()
        object TurnFinished : Action()
        object RegistrySucess: Action()
    }

    sealed class MainAction: BaseAction() {
        object NavigateToRegistries: MainAction()
        object NavigateToObservations: MainAction()
    }

    protected val _mainViewAction: SingleLiveEvent<MainAction> = SingleLiveEvent()
    val mainViewAction: LiveData<MainAction>
        get() = _mainViewAction

    protected val _currentTurn: SingleLiveEvent<Turn?> = SingleLiveEvent()
    val currentTurn: LiveData<Turn?>
        get() = _currentTurn

    protected val _newsRegistry: SingleLiveEvent<List<NewsRegistry>> = SingleLiveEvent()
    val newsRegistry: LiveData<List<NewsRegistry>>
        get() = _newsObservations

    protected val _newsObservations: SingleLiveEvent<List<NewsRegistry>> = SingleLiveEvent()
    val newsObservations: LiveData<List<NewsRegistry>>
        get() = _newsObservations

     fun getCurrentTurn() = launch {
        _viewState.value = State.Loading

        when (val result = vigilantRepository.getTodayTurn(currentUser!!.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _currentTurn.value = result.b

                result.b?.let {
                    if(it.status == Turn.STATE.ACTIVE) {
                        _viewAction.value = Action.TurnStarted

                        startTurnTimer(it.initDate ?: LocalDateTime.now())
                    }
                }
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getNewsRegistry(turnId: Long) = launch {
        _viewState.value = State.Loading

        when (val result = vigilantRepository.getVigilantTurnRegistries(currentUser!!.id, turnId)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _newsObservations.value = result.b
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getNewsObservations(turnId: Long) = launch {
        _viewState.value = State.Loading

        when (val result = vigilantRepository.getVigilantTurnObservations(currentUser!!.id, turnId)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _newsObservations.value = result.b
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addNewRegistry(turnId: Long, type: NewsRegistry.TYPE) = launch {
        _viewState.value = State.Loading

        when (val result = vigilantRepository.addNewTurnRegistry(currentUser!!.id, turnId, type)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.RegistrySucess
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addNewObservations(turnId: Long, description: String) = launch {
        _viewState.value = State.Loading

        when (val result = vigilantRepository.addNewTurnObservation(currentUser!!.id, turnId, description)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.RegistrySucess
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    var turnTimer: CountDownTimer? = null

    private fun startTurnTimer(date: LocalDateTime) {
        var currentDuration = Duration.ofSeconds(ChronoUnit.SECONDS.between(date, LocalDateTime.now()))

        turnTimer?.cancel()

        _viewAction.value = Action.TurnStartedSeconds(currentDuration)

        turnTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {

            override fun onTick(p0: Long) {
                launch {
                    currentDuration = currentDuration.plusSeconds(1)
                    _viewAction.value = Action.TurnStartedSeconds(currentDuration)
                }
            }

            override fun onFinish() {}
        }

        turnTimer!!.start()
    }

    fun startTurn(lat: Double, lon: Double) = launch {
        _viewState.value = State.Loading

        when (val result = vigilantRepository.startTurn(currentUser!!.id, lat, lon)) {
            is Either.Right -> {
                _viewState.value = State.Success

                _currentTurn.value = Turn(result.b, Turn.STATE.ACTIVE, LocalDateTime.now())

                _viewAction.value = Action.TurnStarted
                startTurnTimer(LocalDateTime.now())
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun endTurn(turnId: Long, lat: Double, lon: Double) = launch {
        when (val result = vigilantRepository.finishTurn(currentUser!!.id, turnId, lat, lon)) {
            is Either.Right -> {
                _viewState.value = State.Success

                turnTimer?.cancel()
                _viewAction.value = Action.TurnFinished
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun goToRegistries() {
        _mainViewAction.value = MainAction.NavigateToRegistries
    }

    fun goToObservations() {
        _mainViewAction.value = MainAction.NavigateToObservations
    }

    fun closeSession() = launch {
        userRepository.clearAccessToken()
    }

}