package com.gesecur.app.ui.auth

import android.content.Context
import android.content.Intent
import com.gesecur.app.R
import com.gesecur.app.ui.common.base.BaseActivity

class AuthActivity : BaseActivity(R.layout.activity_auth) {

    companion object {
        fun createIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }

    override fun setupViews() {
        //Nothing
    }

    override fun setupViewModels() {
        //Nothing
    }
}