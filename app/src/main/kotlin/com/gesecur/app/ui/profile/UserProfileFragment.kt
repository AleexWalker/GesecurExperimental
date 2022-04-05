package com.gesecur.app.ui.profile

import android.content.DialogInterface
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentUserProfileBinding
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorActivity
import com.gesecur.app.utils.showConfirm
import org.koin.androidx.viewmodel.ext.android.viewModel

@ToolbarOptions(
    showToolbar = true,
    titleRes = R.string.USER_PROFILE_TITLE,
homeButton = GesecurToolbar.HomeButton.BACK,
)
class UserProfileFragment : BaseFragment(R.layout.fragment_user_profile) {

    private val binding by viewBinding(FragmentUserProfileBinding::bind)

    private val viewModel by viewModel<PersonalViewModel>()

    override fun setupViews() {

        with(binding) {
            optionExpenses.setOnClickListener { navigateToExpenses() }
            optionMileage.setOnClickListener { navigateToMileage() }
            optionCloseSession.setOnClickListener { closeSession() }
        }

        (requireActivity() as OperatorActivity).hideToolbarExtra();
    }

    override fun setupViewModels() {
    }

    private fun navigateToExpenses() {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToPersonalExpensesListFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToMileage() {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToPersonalMileageListFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun closeSession() {
        showConfirm(R.string.PROFILE_CLOSE_SESSION_QUESTION, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(p1 == DialogInterface.BUTTON_POSITIVE) {
                    viewModel.closeSession()
                    navigateToSplash()
                }
            }
        })
    }
}