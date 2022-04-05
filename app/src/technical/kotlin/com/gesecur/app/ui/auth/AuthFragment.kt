package com.gesecur.app.ui.auth

import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.BuildConfig
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentAuthBinding
import com.gesecur.app.domain.models.User
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.operator.OperatorActivity
import com.gesecur.app.ui.vigilant.VigilantActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel by viewModel<AuthViewModel>()

    override fun setupViews() {
        binding.btnLogin.setOnClickListener { doLogin() }

        if(BuildConfig.DEBUG) {
            binding.editEmail.setText("test@gruposimetria.com")
            binding.editPassword.setText("2021")
        }
    }

    override fun setupViewModels() {
        viewModel.viewAction.observe(this) {
            when (it) {
                is AuthViewModel.Action.OnUserLogged -> onUserLogged(it.user)
                is BaseAction.ShowError -> showError(it.error)
                is BaseAction.ShowErrorRes -> showError(getString(it.error))
            }
        }
    }

    override fun stateManagedViewModels() = arrayListOf(viewModel)

    private fun doLogin() {
        with(binding) {
            viewModel.login(
                editEmail.text.toString(),
                editPassword.text.toString()
            )
        }
    }


    private fun onUserLogged(user: User) {
        val intent = if(user.isOperator()) OperatorActivity.createIntent(requireContext()) else VigilantActivity.createIntent(requireContext())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}