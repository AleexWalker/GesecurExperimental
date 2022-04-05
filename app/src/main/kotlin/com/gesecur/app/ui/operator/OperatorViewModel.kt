package com.gesecur.app.ui.operator

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.Either
import arrow.core.getOrElse
import com.gesecur.app.domain.models.*
import com.gesecur.app.domain.repositories.gesecur.GesecurRepository
import com.gesecur.app.domain.repositories.user.UserRepository
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.arch.SingleLiveEvent
import com.gesecur.app.ui.common.arch.State
import com.gesecur.app.ui.common.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class OperatorViewModel(
        private val userRepository: UserRepository,
        private val gesecurRepository: GesecurRepository
) : BaseViewModel() {

    sealed class Action: BaseAction() {
        object OnUserLogged : Action()
        class JobDeleted(val message: String) : Action()
        object JobAdded : Action()
        object JobUpdated : Action()
        object OtherUpdated : Action()
        object JobStarted : Action()
        object IsJobStartedByOther : Action()
        class JobStartedSeconds(val seconds: Duration) : Action()
        object PartClosed : Action()
        object JobFinished : Action()
        object JobFinishedButNotCompleted : Action()
        object MaterialUpdated : Action()
        class OnWorkOrderSelected(val workPart: WorkPart): Action()
        class OnWorkPlaniSelected(val workPlanification: WorkPlanification): Action()
        class OnPlanificationStarted(val partId: Long): Action()
        object PartCloseSuccess : Action()
        object AttachmentAdded: Action()
    }

    protected var currentUser: User? = userRepository.getUser().getOrElse { null }

    protected val _workParts: SingleLiveEvent<List<WorkPart>> = SingleLiveEvent()
    val workParts: LiveData<List<WorkPart>>
        get() = _workParts

    protected val _workPlanis: SingleLiveEvent<List<WorkPlanification>> = SingleLiveEvent()
    val workPlanis: LiveData<List<WorkPlanification>>
        get() = _workPlanis

    protected val _workPlaniDetail: SingleLiveEvent<WorkPlanification> = SingleLiveEvent()
    val workPlaniDetail: LiveData<WorkPlanification>
        get() = _workPlaniDetail

    protected val _codifiedJobs: SingleLiveEvent<List<CodifiedJob>> = SingleLiveEvent()
    val codifiedJobs: LiveData<List<CodifiedJob>>
        get() = _codifiedJobs

    protected val _availableProducts: SingleLiveEvent<List<Product>> = SingleLiveEvent()
    val availableProducts: LiveData<List<Product>>
        get() = _availableProducts

    protected val _workPartDetail: SingleLiveEvent<WorkPart> = SingleLiveEvent()
    val workPartDetail: LiveData<WorkPart>
        get() = _workPartDetail

    protected val _otServices: SingleLiveEvent<List<Service>> = SingleLiveEvent()
    val otServices: LiveData<List<Service>>
        get() = _otServices

    protected val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData()
    val selectedDate: LiveData<LocalDate>
        get() = _selectedDate

    fun selectDateForPart(date: LocalDate) {
        _selectedDate.value = date

        currentUser?.let {
            getUserWorkParts(it.personalId, date)
        }
    }

    fun selectDateForPlani(date: LocalDate) {
        _selectedDate.value = date

        currentUser?.let {
            getUserWorkPlanis(it.personalId, date)
        }
    }

    fun selectWorkPart(workPart: WorkPart) {
        _viewAction.value = Action.OnWorkOrderSelected(workPart)
    }

    fun selectWorkPlani(workPlanification: WorkPlanification) {
        _viewAction.value = Action.OnWorkPlaniSelected(workPlanification)
    }

    private fun getUserWorkParts(userId: Long, date: LocalDate) = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.getWorkParts(userId, date)) {
            is Either.Right -> {
                with(result.b) {
                    _viewState.value = if(isEmpty()) State.Empty else State.Success
                    _workParts.value = this
                }
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getWorkPartDetail(workPartId: Long) = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.getWorkPartDetail(currentUser!!.id, workPartId = workPartId)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _workPartDetail.value = result.b

                with(result.b) {
                    if(isPastPart()) {
                        _viewAction.value = Action.PartClosed
                    }
                    else {
                        if(hasFinishedPart()) {
                            if(confirmationDone)
                                _viewAction.value = Action.PartClosed
                            else if(clientConfirmation == 0)
                                _viewAction.value = Action.JobFinished
                            else
                                _viewAction.value = Action.JobFinishedButNotCompleted
                        }
                        else if(state == WorkPart.STATE.STARTED) {
                            personal.firstOrNull { it.personalId != currentUser!!.personalId
                                    && it.initDate != null && it.endDate == null
                            }?.let {

                                if(it.initDate != null) {
                                    _viewAction.value = Action.IsJobStartedByOther
                                }
                            }

                            personal.firstOrNull { it.personalId == currentUser!!.personalId }
                                ?.let {
                                    if(it.initDate != null) {

                                        if(it.endDate == null) {
                                            _viewAction.value = Action.JobStarted
                                            startJobTimer(it.initDate)
                                        }
                                        else {
                                            if(clientConfirmation == 0)
                                                _viewAction.value = Action.JobFinished
                                            else
                                                _viewAction.value = Action.JobFinishedButNotCompleted
                                        }
                                    }
                                }
                        }
                        else {}
                    }
                }
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getCodifiedJobs() = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.getCodifiedJobs(currentUser!!.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _codifiedJobs.value = result.b
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addJob(partId: Long, codifiedJob: CodifiedJob,
               description: String?, duration: Int?) = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.addJob(currentUser!!.personalId, partId, codifiedJob, description, duration)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.JobAdded
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun deleteJob(job: Job) = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.deleteJob(currentUser!!.id, job)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.JobDeleted(result.b.message)
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getAvailableProducts() = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.getAvailableProducts(currentUser!!.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _availableProducts.value = result.b
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addMaterial(workPartId: Long, material: Product, quantity: Int) = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.addMaterial(currentUser!!.personalId, workPartId, material)) {
            is Either.Right -> {
                _viewState.value = State.Success

                material.quantity = quantity
                material.parteMaterialId = result.b.result
                material.extra = true
                executeMaterialUpdate(workPartId, material)
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun updateMaterial(workPartId: Long, item: Product, quantity: Int) {
        updateItemQuantity(workPartId, item) { partId: Long, any: Any ->
            item.quantity = quantity
            executeMaterialUpdate(partId, item)
        }
    }

    fun updateJobQuantity(workPartId: Long, item: Job, quantity: Int) {
        updateItemQuantity(workPartId, item) { partId: Long, any: Any ->
            item.quantity = quantity
            executeJobUpdateQuantity(partId, item)
        }
    }

    fun updateOtherQuantity(workPartId: Long, item: Other, quantity: Int) {
        updateItemQuantity(workPartId, item) { partId: Long, any: Any ->
            item.quantity = quantity
            executeOtherUpdateQuantity(partId, item)
        }
    }

    val mapItemTimer = hashMapOf<Any, Timer>()

    fun updateItemQuantity(workPartId: Long, item: kotlin.Any, updateUnit : ((Long, Any) -> Unit)) = launch {

        if(mapItemTimer[item] == null) {
            mapItemTimer[item] = createUpdateItemTimer(workPartId, item, updateUnit)
        }
        else {
            val timer = mapItemTimer[item]
            timer?.cancel()

            mapItemTimer[item] = createUpdateItemTimer(workPartId, item, updateUnit)
        }
    }

    private fun createUpdateItemTimer(workPartId: Long, item: Any, updateUnit : ((Long, Any) -> Unit)): Timer {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                mapItemTimer.remove(item)

                updateUnit(workPartId, item)
            }
        }, 500)

        return timer
    }

    private fun executeMaterialUpdate(partId: Long, material: Product) = launch {
        when (val result = gesecurRepository.updateMaterial(partId, material, material.quantity)) {
            is Either.Right -> {
                _viewAction.value = Action.MaterialUpdated
            }

            is Either.Left -> {
                _viewAction.value = BaseAction.ShowError(result.a)

                workPartDetail.value?.id?.let { getWorkPartDetail(it) }
            }
        }
    }



    private fun executeJobUpdateQuantity(partId: Long, job: Job) = launch {
        when (val result = gesecurRepository.updateJobQuantity(currentUser!!.personalId, job)) {
            is Either.Right -> {
                _viewAction.value = Action.JobUpdated
            }

            is Either.Left -> {
                _viewAction.value = BaseAction.ShowError(result.a)

                workPartDetail.value?.id?.let { getWorkPartDetail(it) }
            }
        }
    }

    private fun executeOtherUpdateQuantity(partId: Long, other: Other) = launch {
        when (val result = gesecurRepository.updateOtherQuantity(currentUser!!.personalId, other)) {
            is Either.Right -> {
                _viewAction.value = Action.OtherUpdated
            }

            is Either.Left -> {
                _viewAction.value = BaseAction.ShowError(result.a)

                workPartDetail.value?.id?.let { getWorkPartDetail(it) }
            }
        }
    }



    var jobTimer: CountDownTimer? = null

    private fun startJobTimer(date: LocalDateTime) {
        var currentDuration = Duration.ofSeconds(ChronoUnit.SECONDS.between(date, LocalDateTime.now()))

        jobTimer?.cancel()

        _viewAction.value = Action.JobStartedSeconds(currentDuration)

        jobTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {

            override fun onTick(p0: Long) {
                launch {
                    currentDuration = currentDuration.plusSeconds(1)
                    _viewAction.value = Action.JobStartedSeconds(currentDuration)
                }
            }

            override fun onFinish() {}
        }

        jobTimer!!.start()
    }

    fun startJob(part: WorkPart, lat: Double, lon: Double) = launch {
        _viewState.value = State.Loading

        val partPersonalId = part.personal.firstOrNull { it.personalId == currentUser!!.personalId }?.partPersonalId ?: -1

        when (val result = gesecurRepository.startWork(currentUser!!.personalId, part.id ?: -1, partPersonalId = partPersonalId, lat, lon)) {
            is Either.Right -> {
                _viewState.value = State.Success

                _viewAction.value = Action.IsJobStartedByOther
                _viewAction.value = Action.JobStarted
                startJobTimer(LocalDateTime.now())
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun startPlanification(planificationId: Long) = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.startPlanification(planificationId, currentUser!!.id, currentUser!!.personalId)) {
            is Either.Right -> {
                _viewState.value = State.Success

                _viewAction.value = Action.OnPlanificationStarted(result.b ?: -1L)
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun finishJob(part: WorkPart, lat: Double, lon: Double, extraTime: Int) = launch {
        _viewState.value = State.Loading

        val partPersonalId = part.personal.firstOrNull { it.personalId == currentUser!!.personalId }?.partPersonalId ?: -1

        when (val result = gesecurRepository.finishWork(currentUser!!.personalId, part.id ?: -1, partPersonalId, lat, lon, extraTime)) {
            is Either.Right -> {
                _viewState.value = State.Success

                jobTimer?.cancel()

                part.id?.let {
                    getWorkPartDetail(part.id)
                } ?: run {
                    _viewAction.value = Action.JobFinished
                }
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun closePart(part: WorkPart, dni: String, obs: String, faults: Boolean) = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.closePart(currentUser!!.personalId, part.id ?: -1, dni, obs ?: "", faults)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.PartCloseSuccess
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    private fun getUserWorkPlanis(personalId: Long, date: LocalDate) = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.getPlanificationsByDate(personalId, date)) {
            is Either.Right -> {
                with(result.b) {
                    _viewState.value = if (isEmpty()) State.Empty else State.Success
                    _workPlanis.value = this
                }
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getWorkPlaniDetail(workPlaniId: Long, date: LocalDate) = launch {
        _viewState.value = State.Loading

        when (val result = gesecurRepository.getWorkPlaniDetail(currentUser!!.personalId, workPlaniId = workPlaniId, date)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _workPlaniDetail.value = result.b
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addAttachment(partId: Long, notes: String, file: File?) = launch {
        _viewState.value = State.Loading

        gesecurRepository.addAttachment(currentUser!!.id,
                                        currentUser!!.personalId,
                                        partId,
                                        notes,
                                        file)
            .fold({
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(it)
            }, {
                _viewState.value = State.Success
                _viewAction.value = Action.AttachmentAdded
            })
    }
}