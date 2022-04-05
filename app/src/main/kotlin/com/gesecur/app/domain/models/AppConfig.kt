package com.gesecur.app.domain.models

import android.os.Parcelable
import com.gesecur.app.BuildConfig
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppConfig(
    val minVersion: Long = 1,
) : Parcelable {

    companion object {
        const val KEY_APP_MIN_VERSION = "app_min_version"
    }

    fun shouldUpdateApp(): Boolean {
        return BuildConfig.VERSION_CODE < minVersion
    }
}
