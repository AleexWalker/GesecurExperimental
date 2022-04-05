package com.gesecur.app.ui.common.dialog

import android.content.Context
import android.os.Bundle
import com.gesecur.app.R
import com.gesecur.app.ui.common.base.BaseDialog

class LoadingDialog(context: Context) : BaseDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)

        setCancelable(false)
    }
}