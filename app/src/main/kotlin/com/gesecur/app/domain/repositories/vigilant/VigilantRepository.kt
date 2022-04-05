package com.gesecur.app.domain.repositories.vigilant

import arrow.core.Either
import com.gesecur.app.data.gesecur.responses.BaseResponse
import com.gesecur.app.data.gesecur.responses.OperationsResponse
import com.gesecur.app.domain.models.*

interface VigilantRepository {

    suspend fun getVigilantIncidences(userId: Long): Either<GesecurError, List<VigilantIncidence>>
    suspend fun getVigilantIncidenceTypes(vigilantId: Long): Either<GesecurError, List<IncidenceType>>
    suspend fun getVigilantTurns(vigilantId: Long): Either<GesecurError, List<Turn>>
    suspend fun getVigilantTurnRegistries(vigilantId: Long, turnId: Long): Either<GesecurError, List<NewsRegistry>>
    suspend fun getVigilantTurnObservations(vigilantId: Long, turnId: Long): Either<GesecurError, List<NewsRegistry>>
    suspend fun addNewTurnRegistry(vigilantId: Long, turnId: Long, typeId: NewsRegistry.TYPE): Either<GesecurError, OperationsResponse>
    suspend fun addNewTurnObservation(vigilantId: Long, turnId: Long, description: String): Either<GesecurError, OperationsResponse>
    suspend fun getTodayTurn(vigilantId: Long): Either<GesecurError, Turn?>
    suspend fun startTurn(vigilantId: Long, lat: Double, lon: Double): Either<GesecurError, Long>
    suspend fun finishTurn(vigilantId: Long, turnId: Long, lat: Double, lon: Double): Either<GesecurError, OperationsResponse>}