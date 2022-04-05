package com.gesecur.app.domain.models

import android.content.Context
import androidx.annotation.StringRes
import com.gesecur.app.R

import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class GesecurError(
    val type: Type = Type.UNKNOWN,
    val throwable: Throwable = Throwable(),
    val errorMessage: ErrorMessage = ErrorMessage()
) : Error(throwable) {
    companion object {
        fun getHttpError(exception: HttpException, response: Response<*>?): GesecurError {
            try {
                var message: String? = null
                val jsonError = response?.errorBody()?.string()
                val jsonObject = JSONObject(jsonError!!)

                if (jsonObject.keys().hasNext()) {
                    val errorKey = jsonObject.keys().next()

                    message = try {
                        val jsonArray = jsonObject.getJSONArray(errorKey)
                        jsonArray[0].toString()
                    } catch (e: Exception) {
                        if (jsonObject.has("message")) {
                            jsonObject.getString("message")
                        } else {
                            jsonObject.getString(errorKey)
                        }
                    }
                }

                return GesecurError(
                    type = Type.HTTP,
                    throwable = exception,
                    errorMessage = ErrorMessage(messageString = message ?: response.errorBody()?.string())
                )
            } catch (e: Exception) {
                return GesecurError(type = Type.HTTP, throwable = exception)
            }
        }
    }

    enum class Type {
        HTTP,
        NO_USER,
        NO_INTERNET,
        TIMEOUT,
        UNKNOWN;
    }

    fun getMessageError(context: Context): String? {
        return when {
            errorMessage.messageString != null -> {
                errorMessage.messageString
            }
            errorMessage.messageId != -1 -> {
                context.getString(errorMessage.messageId)
            }
            else -> {
                localizedMessage
            }
        }
    }

    data class ErrorMessage(val messageString: String? = null, @StringRes val messageId: Int = -1)
}

fun Throwable.toError(): GesecurError {
    Timber.e(this)

    return when (this) {
        is HttpException -> GesecurError.getHttpError(this, response())

        is UnknownHostException -> GesecurError(
            type = GesecurError.Type.NO_INTERNET,
            throwable = this,
            errorMessage = GesecurError.ErrorMessage(messageId = R.string.ERROR_NO_INTERNET)
        )

        is TimeoutException -> GesecurError(
            type = GesecurError.Type.TIMEOUT,
            throwable = this,
            errorMessage = GesecurError.ErrorMessage(messageId = R.string.ERROR_TIMEOUT)
        )

        else -> GesecurError(
            type = GesecurError.Type.UNKNOWN,
            throwable = this,
            errorMessage = GesecurError.ErrorMessage(messageString = this.localizedMessage, messageId = R.string.ERROR_UNKNOWN)
        )
    }
}