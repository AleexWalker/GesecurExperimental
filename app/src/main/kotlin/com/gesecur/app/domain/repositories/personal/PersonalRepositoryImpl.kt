package com.gesecur.app.domain.repositories.personal

import arrow.core.Either
import com.gesecur.app.data.gesecur.GesecurService
import com.gesecur.app.data.gesecur.responses.OperationsResponse
import com.gesecur.app.domain.models.*
import com.gesecur.app.domain.repositories.personal.PersonalRepository
import com.gesecur.app.utils.toGesecurFormat
import com.gesecur.app.utils.toServerFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PersonalRepositoryImpl(
    private val service: GesecurService
) : PersonalRepository {


    override suspend fun getUserExpenses(
        userId: Long,
        personalId: Long
    ): Either<GesecurError, List<Expense>> {
        return withContext(Dispatchers.IO) {
            Either.catch { service.getUserExpenses(userId,personalId).result?.sortedByDescending { it.date } ?: arrayListOf()
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun getUserExpenseTypes(userId: Long): Either<GesecurError, List<ExpenseType>> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.getUserExpenseTypes(userId).result
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun addUserExpense(
        userId: Long,
        personalId: Long,
        expense: Expense,
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

                val result = service.addUserExpense(userId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    (expense.partId?.toString() ?: "").toRequestBody("text/plain".toMediaTypeOrNull()),
                    personalId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    expense.date!!.toServerFormat().toRequestBody("text/plain".toMediaTypeOrNull()),
                    expense.quantity.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    expense.typeId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    expense.description.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    expense.price.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
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

    override suspend fun deleteExpense(
        userId: Long,
        expenseId: Long
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                val response = service.deleteExpense(userId, expenseId)

                if(response.status != 200) {
                    throw Throwable(response.message)
                }
                else {
                    response
                }
            }
                .mapLeft { it.toError() }
        }
    }

    override suspend fun getUserMileage(
        userId: Long,
        personalId: Long
    ): Either<GesecurError, List<Mileage>> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.getUserMileage(userId,personalId).result?.sortedByDescending { it.date } ?: arrayListOf()
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun getUserVehicles(userId: Long): Either<GesecurError, List<Vehicle>> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                service.getUserVehicles(userId).result
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun addUserMileage(
        userId: Long,
        personalId: Long,
        mileage: Mileage,
        vehicle: Vehicle,
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

                val result = service.addUserMileage(userId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    mileage.partId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    personalId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    (mileage.date?.toServerFormat() ?: "").toRequestBody("text/plain".toMediaTypeOrNull()),
                    mileage.kmIni.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    mileage.kmFin.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    vehicle.id.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    mileage.description.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
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

    override suspend fun deleteMileage(
        userId: Long,
        mileageId: Long
    ): Either<GesecurError, OperationsResponse> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                val response = service.deleteMileage(userId, mileageId)

                if(response.status != 200) {
                    throw Throwable(response.message)
                }
                else {
                    response
                }
            }
                .mapLeft { it.toError() }
        }
    }
}