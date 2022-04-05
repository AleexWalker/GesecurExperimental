package com.gesecur.app.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gesecur.app.BuildConfig
import com.gesecur.app.R
import com.gesecur.app.ui.auth.AuthActivity
import com.gesecur.app.ui.common.base.BaseActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.crashes.Crashes


class SplashActivity : BaseActivity(R.layout.activity_splash) {

    companion object {
        fun createIntent(context: Context) = Intent(context, SplashActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(BuildConfig.FLAVOR == "technical") {
            AppCenter.start(
                application, "afa055b0-ac1f-4fad-839a-f2545efd6e12",
                Crashes::class.java
            )
        }
        else {
            AppCenter.start(
                application, "38d2d3c8-e939-434d-a244-dfe1b67ceb50",
                Crashes::class.java
            )
        }
    }

    override fun setupViews() {
        //Nothing
    }

    override fun setupViewModels() {
        //Nothing
    }
}