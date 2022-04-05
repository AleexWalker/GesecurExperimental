package com.gesecur.app.ui.common.base

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.gesecur.app.R
import com.gesecur.app.domain.models.GesecurError
import com.gesecur.app.ui.common.arch.State
import com.gesecur.app.ui.common.bottombar.BottomBarOptions
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.splash.SplashActivity
import com.gesecur.app.utils.finishActivity
import com.gesecur.app.utils.toast

abstract class BaseFragment(
    @LayoutRes layout: Int = ResourcesCompat.ID_NULL
) : Fragment(layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupBottomBar()

        setupViews()
        setupViewModels()

        stateManagedViewModels()?.forEach {
            it.viewState.observe(viewLifecycleOwner) {
                when (it) {
                    is State.Loading -> {
                        showLoadingDialog()
                    }
                    is State.Success -> {
                        hideLoadingDialog()
                        showSuccessState()
                    }
                    is State.Empty -> {
                        hideLoadingDialog()
                        showEmptyState()
                    }
                }
            }
        }
    }

    abstract fun setupViews()
    abstract fun setupViewModels()

    open fun stateManagedViewModels(): List<BaseViewModel>? = null

    open fun showEmptyState() {}

    open fun showSuccessState() {}

    /**
     * Show loading dialog
     */
    protected fun showLoadingDialog() {
        (activity as? BaseActivity)?.showLoadingDialog()
    }

    /**
     * Hide loading dialog
     */
    protected fun hideLoadingDialog() {
        (activity as? BaseActivity)?.hideLoadingDialog()
    }

    /**
     * Setup bottom bar
     */
    private fun setupBottomBar() {
        val annotation = javaClass.getAnnotation(BottomBarOptions::class.java)

        if (annotation != null) {
            showBottomBar(annotation.showBottomBar)
        } else {
            showBottomBar(false)
        }
    }

    /**
     * Show bottom bar
     */
    private fun showBottomBar(show: Boolean) {
        (activity as? BaseActivity)?.showBottomBar(show)
    }

    private fun setupToolbar() {
        val annotation = javaClass.getAnnotation(ToolbarOptions::class.java)

        if (annotation != null) {
            showToolbar(annotation.showToolbar)
            setHomeButton(annotation.homeButton)
            setTitle(annotation.titleRes, annotation.title)

        } else {
            showToolbar(false)
        }
    }

    private fun showToolbar(show: Boolean) {
        (activity as? BaseActivity)?.showToolbar(show)
    }

    private fun setHomeButton(button: GesecurToolbar.HomeButton) {
        (activity as? BaseActivity)?.setHomeButton(button)
    }

    protected fun setTitle(titleId: Int = Resources.ID_NULL, title: String = "") {
        if(title.isNotEmpty())
            activity?.title = title
        else if(titleId != ResourcesCompat.ID_NULL)
            activity?.setTitle(titleId)
    }

    fun showError(message: GesecurError) {
        message.errorMessage?.messageString?.let {
            showError(it)
        } ?: kotlin.run {
            showError(getString(R.string.ERROR_UNKNOWN))
        }
    }

    fun showError(message: String) {
        toast(message)
    }

    fun navigateToSplash() {
        val intent = SplashActivity.createIntent(requireContext())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finishActivity()
    }
}