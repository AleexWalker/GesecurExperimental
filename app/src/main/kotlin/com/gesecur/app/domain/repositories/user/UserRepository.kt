package com.gesecur.app.domain.repositories.user

import arrow.core.Either
import com.gesecur.app.domain.models.AccessToken
import com.gesecur.app.domain.models.GesecurError
import com.gesecur.app.domain.models.User

interface UserRepository {

    fun getAccessToken(): String?
    fun saveAccessToken(accessToken: String)
    fun clearAccessToken()
    fun logout()

    suspend fun isUserLogged(): Either<GesecurError, Boolean>
    suspend fun login(email: String, password: String): Either<GesecurError, User?>
    suspend fun loginVigilant(code: String): Either<GesecurError, User?>

    fun getUser(): Either<GesecurError, User>
    fun saveUser(user: User): Either<GesecurError, Boolean>
}