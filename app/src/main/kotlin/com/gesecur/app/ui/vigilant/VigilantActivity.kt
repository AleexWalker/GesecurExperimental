package com.gesecur.app.ui.vigilant

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.ActivityServicesVigilantBinding
import com.gesecur.app.databinding.ActivityVigilantBinding
import com.gesecur.app.ui.common.base.BaseActivity
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.utils.showConfirm
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class VigilantActivity : BaseActivity(R.layout.activity_services_vigilant) {

    companion object {
        fun createIntent(context: Context) = Intent(context, VigilantActivity::class.java)
    }


    private val binding by viewBinding(ActivityServicesVigilantBinding::bind)

    override fun setupViews() {
        toolbar = binding.toolbar
    }

    override fun setupViewModels() {
        //Nothing
    }

    fun setToolbarExtra(image: Int, callback: () -> Unit) {
        binding.toolbar.setExtra(image, callback)
    }

    fun hideToolbarExtra() {
        binding.toolbar.hideExtra()
    }
}