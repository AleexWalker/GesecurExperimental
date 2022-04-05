package com.gesecur.app.domain.repositories.gesecur

import arrow.core.Either
import com.gesecur.app.data.gesecur.GesecurService
import com.gesecur.app.data.gesecur.responses.OperationsResponse
import com.gesecur.app.domain.models.*
import com.gesecur.app.domain.repositories.vigilant.VigilantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class VigilantRepositoryImpl(
    private val service: GesecurService
) : VigilantRepository {


    override suspend fun getVigilantIncidences(userId: Long): Either<GesecurError, List<VigilantIncidence>> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getVigilantIncidences(userId).result
                .sortedByDescending { it.id }
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun getVigilantIncidenceTypes(userId: Long): Either<GesecurError, List<IncidenceType>> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getVigilantIncidenceTypes(userId).result
                .map { IncidenceType(it.id, it.description) }
            }
                    .mapLeft { it.toError() }
        }
    }

    override suspend fun getVigilantTurns(userId: Long): Either<GesecurError, List<Turn>> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getVigilantTurns(userId).result }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun getVigilantTurnRegistries(
        userId: Long,
        turnId: Long
    ): Either<GesecurError, List<NewsRegistry>> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.getTurnIdRegistries(userId, turnId)
                    .result
                    .filter { it.type != null }
                    .sortedByDescending { it.date }
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun getVigilantTurnObservations(
        userId: Long,
        turnId: Long
    ): Either<GesecurError, List<NewsRegistry>> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.getTurnIdRegistries(userId, turnId)
                    .result
                    .filter { it.type == null }
                    .sortedByDescending { it.date }
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun addNewTurnRegistry(
        personalId: Long,
        turnId: Long,
        typeId: NewsRegistry.TYPE
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.addNewTurnRegistry(personalId, turnId, typeId.id) }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun addNewTurnObservation(
        personalId: Long,
        turnId: Long,
        description: String
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.addNewTurnObservation(personalId, turnId, description) }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun getTodayTurn(userId: Long): Either<GesecurError, Turn?> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.getVigilantTurns(userId).result.firstOrNull {
                    it.initDate?.toLocalDate() == LocalDate.now() &&
                            it.status != Turn.STATE.FINISHED
                }
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun startTurn(
        vigilantId: Long,
        lat: Double,
        lon: Double
    ): Either<GesecurError, Long> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.startTurn(
                    vigilantId,
                    lat, lon
                ).result
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun finishTurn(
        vigilantId: Long,
        turnId: Long,
        lat: Double,
        lon: Double
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.finishTurn(
                    vigilantId, turnId,
                    lat, lon
                )
            }
                .mapLeft { it.toError() }
        }
    }
}