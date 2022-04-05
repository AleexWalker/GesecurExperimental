package com.gesecur.app.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.gesecur.app.R
import com.gesecur.app.ui.auth.AuthActivity
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.operator.OperatorActivity
import com.gesecur.app.ui.vigilant.VigilantActivity
import com.gesecur.app.utils.finishActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel by viewModel<SplashViewModel>()

    override fun setupViews() {
        checkPermissions()
    }

    @SuppressLint("MissingPermission")
    private val locationPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            viewModel.initSplash()
        }
    }

    private fun checkPermissions() {
        locationPermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun setupViewModels() {
        viewModel.viewAction.observe(this) {
            when (it) {
                is SplashViewModel.Action.NavigateToVigilantHome -> navigateToVigilantHome()
                is SplashViewModel.Action.NavigateToOperatorHome -> navigateToOperatorHome()
                is SplashViewModel.Action.NavigateToAuth -> navigateToAuth()
            }
        }
    }

    private fun navigateToVigilantHome() {
        val intent = VigilantActivity.createIntent(requireContext())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finishActivity()
    }

    private fun navigateToOperatorHome() {
        val intent = OperatorActivity.createIntent(requireContext())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finishActivity()
    }

    private fun navigateToAuth() {
        val intent = AuthActivity.createIntent(requireContext())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finishActivity()
    }
}