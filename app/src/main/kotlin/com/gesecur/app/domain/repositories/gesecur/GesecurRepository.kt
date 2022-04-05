package com.gesecur.app.domain.repositories.gesecur

import arrow.core.Either
import com.gesecur.app.data.gesecur.responses.BaseResponse
import com.gesecur.app.data.gesecur.responses.OperationsResponse
import com.gesecur.app.domain.models.*
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

interface GesecurRepository {

    suspend fun getWorkParts(userId: Long, date: LocalDate): Either<GesecurError, List<WorkPart>>
    suspend fun getWorkPartDetail(userId: Long, workPartId: Long): Either<GesecurError, WorkPart>
    suspend fun getWorkOrderServices(
        userId: Long,
        workOrderId: Long
    ): Either<GesecurError, List<Service>>

    suspend fun getCodifiedJobs(userId: Long): Either<GesecurError, List<CodifiedJob>>
    suspend fun addJob(
        workOrderId: Long, partId: Long, codifiedJob: CodifiedJob,
        description: String?, duration: Int?
    ): Either<GesecurError, Boolean>

    suspend fun deleteJob(userId: Long, job: Job): Either<GesecurError, OperationsResponse>
    suspend fun updateJobQuantity(
        personalId: Long,
        job: Job
    ): Either<GesecurError, OperationsResponse>

    suspend fun updateOtherQuantity(
        personalId: Long,
        other: Other
    ): Either<GesecurError, OperationsResponse>

    suspend fun getAvailableProducts(userId: Long): Either<GesecurError, List<Product>>
    suspend fun addMaterial(
        personalId: Long,
        workPartId: Long,
        product: Product
    ): Either<GesecurError, BaseResponse<Long>>

    suspend fun updateMaterial(
        workPartId: Long,
        product: Product,
        quantity: Int
    ): Either<GesecurError, OperationsResponse>

    suspend fun startPlanification(planiId: Long,  userId: Long, personalId: Long): Either<GesecurError, Long?>
    suspend fun startWork(
        personalId: Long,
        partId: Long,
        partPersonalId: Long,
        lat: Double,
        lon: Double
    ): Either<GesecurError, OperationsResponse>

    suspend fun finishWork(
        personalId: Long,
        partId: Long,
        partPersonalId: Long,
        lat: Double,
        lon: Double,
        extraTime: Int
    ): Either<GesecurError, OperationsResponse>

    suspend fun closePart(
        personalId: Long,
        partId: Long,
        dni: String,
        observations: String,
        faults: Boolean
    ): Either<GesecurError, OperationsResponse>


    /**
     * Planifications
     */

    suspend fun getPlanificationsByDate(
        personalId: Long,
        date: LocalDate
    ): Either<GesecurError, List<WorkPlanification>>

    suspend fun getWorkPlaniDetail(userId: Long, workPlaniId: Long, date: LocalDate): Either<GesecurError, WorkPlanification>

    suspend fun addAttachment(
        userId: Long,
        personalId: Long,
        partId: Long,
        notes: String,
        file: File?
    ): Either<GesecurError, OperationsResponse>
}