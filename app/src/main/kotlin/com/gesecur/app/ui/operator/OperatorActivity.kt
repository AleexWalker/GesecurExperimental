package com.gesecur.app.ui.operator

import android.content.Context
import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.ActivityOperatorBinding
import com.gesecur.app.databinding.ActivityVigilantBinding
import com.gesecur.app.ui.common.base.BaseActivity
import com.gesecur.app.ui.common.toolbar.ToolbarOptions

class OperatorActivity : BaseActivity(R.layout.activity_operator) {

    companion object {
        fun createIntent(context: Context) = Intent(context, OperatorActivity::class.java)
    }

    private val binding by viewBinding(ActivityOperatorBinding::bind)

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