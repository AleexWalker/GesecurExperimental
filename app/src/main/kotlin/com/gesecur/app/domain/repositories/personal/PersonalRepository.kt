package com.gesecur.app.domain.repositories.personal

import arrow.core.Either
import com.gesecur.app.data.gesecur.responses.BaseResponse
import com.gesecur.app.data.gesecur.responses.OperationsResponse
import com.gesecur.app.domain.models.*
import java.io.File

interface PersonalRepository {
    suspend fun getUserExpenses(userId: Long, personalId: Long): Either<GesecurError, List<Expense>>
    suspend fun getUserExpenseTypes(userId: Long): Either<GesecurError, List<ExpenseType>>
    suspend fun addUserExpense(userId: Long, personalId: Long, expense: Expense, file: File?): Either<GesecurError, OperationsResponse>
    suspend fun deleteExpense(userId: Long, expenseId: Long): Either<GesecurError, OperationsResponse>

    suspend fun getUserMileage(userId: Long, personalId: Long): Either<GesecurError, List<Mileage>>
    suspend fun getUserVehicles(userId: Long): Either<GesecurError, List<Vehicle>>
    suspend fun addUserMileage(userId: Long, personalId: Long, mileage: Mileage, vehicle: Vehicle, file: File?): Either<GesecurError, OperationsResponse>
    suspend fun deleteMileage(userId: Long, mileageId: Long): Either<GesecurError, OperationsResponse>
}