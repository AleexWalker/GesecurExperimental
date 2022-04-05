package com.gesecur.app.domain.repositories.incidence

import arrow.core.Either
import com.gesecur.app.data.gesecur.GesecurService
import com.gesecur.app.data.gesecur.responses.OperationsResponse
import com.gesecur.app.domain.models.GesecurError
import com.gesecur.app.domain.models.Incidence
import com.gesecur.app.domain.models.IncidenceType
import com.gesecur.app.domain.models.toError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class IncidenceRepositoryImpl(
    private val service: GesecurService
) : IncidenceRepository {


    override suspend fun getIncidences(userId: Long): Either<GesecurError, List<Incidence>> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getVigilantIncidences(userId).result
                .sortedByDescending { it.id }
                .map {
                    Incidence(it.id, it.codeId.toLong(), it.codeDesc, it.address,
                                it.latitude, it.longitude, it.description, it.date, it.file)
                }}
                .mapLeft { it.toError() }
        }
    }

    override suspend fun getIncidenceTypes(userId: Long): Either<GesecurError, List<IncidenceType>> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getVigilantIncidenceTypes(userId).result
                .map { IncidenceType(it.id, it.description) }}
                .mapLeft { it.toError() }
        }
    }

    override suspend fun addIncidence(
        vigilantId: Long,
        userId: Long,
        incidence: Incidence,
        file: File?
    ): Either<GesecurError, OperationsResponse> {

        return withContext(Dispatchers.IO) {

            Either.catch {
                val part = file?.let {
                    val extension = it.name.substring(it.name.indexOf(".") + 1)

                    val requestFile = it.asRequestBody("image/$extension".toMediaType())
                    MultipartBody.Part.createFormData("file", it.name, requestFile)
                } ?: run {
                    MultipartBody.Part.createFormData("file", "", "".toRequestBody("text/plain".toMediaType()))
                }

                val result = service.addVigilantIncidence(
                    incidence.codeId!!.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    incidence.description.toRequestBody("text/plain".toMediaTypeOrNull()),
                    incidence.address.toRequestBody("text/plain".toMediaTypeOrNull()),
                    incidence.latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    incidence.longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    vigilantId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    part
                )

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