package com.gesecur.app.domain.repositories.gesecur

import arrow.core.Either
import com.gesecur.app.data.gesecur.GesecurService
import com.gesecur.app.data.gesecur.responses.BaseResponse
import com.gesecur.app.data.gesecur.responses.OperationsResponse
import com.gesecur.app.domain.models.*
import com.gesecur.app.utils.toServerFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

class GesecurRepositoryImpl(
    private val service: GesecurService
) : GesecurRepository {


    override suspend fun getWorkParts(userId: Long, date: LocalDate): Either<GesecurError, List<WorkPart>> {
        return withContext(Dispatchers.IO) {
            //TODO poner userId
            Either.catch { service.getWorkParts(userId, date.toServerFormat()).result }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun getWorkPartDetail(userId: Long, workPartId: Long): Either<GesecurError, WorkPart> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getWorkPartDetail(userId, workPartId).result }
                    .mapLeft { it.toError() }
        }
    }

    override suspend fun getWorkOrderServices(userId: Long, workOrderId: Long): Either<GesecurError, List<Service>> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getWorkOrderServices(userId, workOrderId).result }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun getCodifiedJobs(userId: Long): Either<GesecurError, List<CodifiedJob>> {
        return withContext(Dispatchers.IO) {
            arrow.core.Either.catch { service.getCodifiedJobs(userId).result }
                    .mapLeft { it.toError() }
        }
    }

    override suspend fun getAvailableProducts(userId: Long): Either<GesecurError, List<Product>> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getAvailableProducts(userId).result }
                    .mapLeft { it.toError() }
        }
    }

    override suspend fun addJob(personalId: Long, partId: Long, codifiedJob: CodifiedJob,
                                description: String?, duration: Int?): Either<GesecurError, Boolean> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                val createdJob = service.addJob(
                        personalId, codifiedJob.id, partId, description ?: "",duration ?: 0
                )

                true
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun updateJobQuantity(
        userId: Long,
        job: Job
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                val result = service.updateJob(
                    userId,
                    job.partId!!,
                    job.id,
                    job.workPartId,
                    job.duration ?: 0,
                    job.quantity,
                    job.extraAsInt() )

                if(result.status == 200) {
                    result
                }
                else {
                    throw Throwable(result.message)
                }
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun updateOtherQuantity(
        personalId: Long,
        other: Other
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                val result = service.updateOtherQuantity(
                    other.partId!!,
                    other.otherPartId!!,
                    other.quantity
                )

                if (result.status == 200) {
                    result
                } else {
                    throw Throwable(result.message)
                }
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun deleteJob(userId: Long, job: Job): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.deleteJob(userId, job.partId!!, job.id , job.workPartId)
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun addMaterial(personalId: Long, workPartId: Long, product: Product): Either<GesecurError, BaseResponse<Long>> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.addMaterial(
                    personalId,
                    product.id,
                    workPartId
                )
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun updateMaterial(workPartId: Long, product: Product, quantity: Int): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.updateMaterial(
                    workPartId,
                    product.parteMaterialId,
                    quantity,
                    product.extraAsInt()
                )
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun startPlanification(planiId: Long, user: Long, personalId: Long): Either<GesecurError, Long?> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                with(service.startPlanification(planiId, user, personalId)) {
                    if(status == 200)
                        return@with result
                    else {
                        throw Throwable(message ?: "Error")
                    }
                }
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun startWork(
        personalId: Long,
        partId: Long,
        partPersonalId: Long,
        lat: Double,
        lon: Double
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.startWork(
                    personalId, partId,
                    partPersonalId,
                    lat, lon
                )
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun finishWork(
        personalId: Long,
        partId: Long,
        partPersonalId: Long,
        lat: Double,
        lon: Double,
        extraTime: Int
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.finishWork(
                    personalId, partId,
                    partPersonalId,
                    lat, lon, extraTime
                )
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun closePart(
        personalId: Long,
        partId: Long,
        dni: String,
        observations: String,
        faults: Boolean
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.closePart(
                    personalId = personalId,
                    partId = partId,
                    dni = dni,
                    observaciones = observations,
                    faults = if(faults) 0 else 1
                )
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun getPlanificationsByDate(
        personalId: Long,
        date: LocalDate
    ): Either<GesecurError, List<WorkPlanification>> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.getPlanificationsByDate(
                    personalId, date.toServerFormat()
                ).result
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun getWorkPlaniDetail(userId: Long, workPlaniId: Long, date: LocalDate): Either<GesecurError, WorkPlanification> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getPlanificationDetailByDate(userId, workPlaniId, date.toServerFormat()).result }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun addAttachment(
        userId: Long,
        personalId: Long,
        partId: Long,
        notes: String,
        file: File?
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {

                val part = file?.let {
                    val extension = it.name.substring(it.name.indexOf(".") + 1)

                    val requestFile = it.asRequestBody("image/${extension.replace("jpg", "jpeg")}".toMediaType())
                    MultipartBody.Part.createFormData("file", it.name, requestFile)
                } ?: run {
                    MultipartBody.Part.createFormData("file", "", "".toRequestBody("text/plain".toMediaType()))
                }

                val result = service.addAttachment(
                    userId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    partId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    personalId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    (LocalDateTime.now().toServerFormat() ?: "").toRequestBody("text/plain".toMediaTypeOrNull()),
                    notes.toRequestBody("text/plain".toMediaTypeOrNull()),
                    part)

                if(result.status != 200) {
                    throw Throwable(result.message)
                }
                else {
                    result
                }
            }
            .mapLeft { it.toError() }
        }
    }
}