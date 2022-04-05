package com.gesecur.app.domain.repositories.incidence

import arrow.core.Either
import com.gesecur.app.data.gesecur.responses.OperationsResponse
import com.gesecur.app.domain.models.*
import java.io.File

interface IncidenceRepository {

    suspend fun getIncidences(personalId: Long): Either<GesecurError, List<Incidence>>
    suspend fun getIncidenceTypes(userId: Long): Either<GesecurError, List<IncidenceType>>
    suspend fun addIncidence(personalId: Long, userId: Long, incidence: Incidence, file: File?): Either<GesecurError, OperationsResponse>
}