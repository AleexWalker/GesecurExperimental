package com.gesecur.app.ui.common.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gesecur.app.R
import com.gesecur.app.ui.common.dialog.LoadingDialog
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.utils.showConfirm

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    private lateinit var loadingDialog: LoadingDialog
    protected var bottomBar: BottomNavigationView? = null
    protected var toolbar: GesecurToolbar? = null
        set(value) {
            field = value
            setSupportActionBar(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        setupViewModels()
    }

    abstract fun setupViews()
    abstract fun setupViewModels()

    fun showLoadingDialog() {
        if (isFinishing || isDestroyed) {
            return
        }

        if (!::loadingDialog.isInitialized) {
            loadingDialog = LoadingDialog(this)
        }

        if (!loadingDialog.isShowing) {
            loadingDialog.show()
        }
    }

    fun hideLoadingDialog() {
        if (isFinishing || isDestroyed) {
            return
        }
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    fun showBottomBar(show: Boolean) {
        bottomBar?.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun setHomeButton(button: GesecurToolbar.HomeButton) {
        val res = when (button) {
            GesecurToolbar.HomeButton.NONE -> {
                null
            }
            GesecurToolbar.HomeButton.BACK -> {
                R.drawable.ic_arrow_left
            }
        }

        setNavigationUpButton(res)
    }

    private fun setNavigationUpButton(@DrawableRes icon: Int?) {
        icon?.let {
            supportActionBar?.apply {
                setHomeAsUpIndicator(it)
                setDisplayHomeAsUpEnabled(true)
            }
        } ?: run {
            supportActionBar?.apply {
                setHomeAsUpIndicator(null)
                setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun showToolbar(show: Boolean) {
        toolbar?.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setToolbarType(type: GesecurToolbar.Type) {
        toolbar?.type = type
    }

    override fun setTitle(titleId: Int) {
        super.setTitle(titleId)
        toolbar?.setTitle(titleId)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        toolbar?.title = title
    }


}