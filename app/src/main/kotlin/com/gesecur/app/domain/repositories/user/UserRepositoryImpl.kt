package com.gesecur.app.domain.repositories.user

import android.content.SharedPreferences
import arrow.core.Either
import com.gesecur.app.data.gesecur.GesecurService
import com.gesecur.app.domain.models.GesecurError
import com.gesecur.app.domain.models.User
import com.gesecur.app.domain.models.toError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.reflect.GenericArrayType

class UserRepositoryImpl(
    private val preferences: SharedPreferences,
    private val service: GesecurService
) : UserRepository {

    companion object {
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KEY_USER = "USER"
    }

    /**
     * Access token
     */

    override fun getAccessToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun saveAccessToken(token: String) {
        preferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    override fun clearAccessToken() {
        preferences.edit().remove(KEY_ACCESS_TOKEN).commit()
    }

    override fun logout() {
        preferences.edit().clear().commit()
    }

    override fun getUser(): Either<GesecurError, User> {
        val json = preferences.getString(KEY_USER, null)
        return json?.let {
                Either.right(Json.decodeFromString(it))
            } ?: run {
                clearAccessToken()
                Either.left(GesecurError(GesecurError.Type.UNKNOWN))
        }
    }

    override fun saveUser(user: User): Either<GesecurError, Boolean> {
        val json = Json.encodeToString(user)
        preferences.edit().putString(KEY_USER, json).apply()

        return Either.right(true)
    }

    override suspend fun isUserLogged(): Either<GesecurError, Boolean> {
        return Either.right(getAccessToken() != null)
    }

    override suspend fun login(email: String, password: String): Either<GesecurError, User?> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                return@catch service.login(email, password).result.let {
                    if(it.token.isNotEmpty()) {
                        saveAccessToken(it.token)

                        val user = service.getCurrentUser(it.user.id).result
                        user.role = User.ROLE.OPERATOR

                        saveUser(user)
                        user
                    }
                    else {
                        null
                    }
                }
            }
            .mapLeft { it.toError() }
        }
    }

    override suspend fun loginVigilant(code: String): Either<GesecurError, User?> {
        return withContext(Dispatchers.IO) {
            Either.catch {
                return@catch service.loginVigilante(code).result.let {
                    if(it.token.isNotEmpty()) {
                        saveAccessToken(it.token)

                        val user = service.getCurrentVigilante(it.user.id).result
                        user.role = User.ROLE.VIGILANT

                        saveUser(user)
                        user
                    }
                    else {
                        null
                    }
                }
            }
                .mapLeft { it.toError() }
        }
    }

    //    override suspend fun getData(): Either<GesecurError, Unit> {
//        return withContext(Dispatchers.IO) {
//            Either.catch { service.createDevice() }
//                .mapLeft { it.toError() }
//        }
//    }
}