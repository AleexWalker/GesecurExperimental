package com.gesecur.app.ui.profile.mileage

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentPersonalMileageBinding
import com.gesecur.app.domain.models.Mileage
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.profile.PersonalViewModel
import com.gesecur.app.ui.profile.expense.PersonalExpensesListFragmentDirections
import com.gesecur.app.utils.showConfirm
import com.gesecur.app.utils.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = true,
    homeButton = GesecurToolbar.HomeButton.BACK,
titleRes = R.string.PROFILE_MILEAGE)
class PersonalMileageListFragment : BaseFragment(R.layout.fragment_personal_mileage) {

    private val binding by viewBinding(FragmentPersonalMileageBinding::bind)
    private val viewModel by sharedViewModel<PersonalViewModel>()

    private val adapter = PersonalMileagesAdapter()

    override fun setupViews() {

        with(binding) {
            adapter.onItemDeleteClick = { expense ->
               deleteMileage(expense)
            }

            rvMileages.adapter = adapter
            rvMileages.layoutManager = LinearLayoutManager(requireContext())

            btnAdd.setOnClickListener { navigateToAddMileage() }
        }
    }

    override fun setupViewModels() {
        viewModel.mileage.observe(viewLifecycleOwner, {
            onMileagesLoaded(it)
        })

        viewModel.viewAction.observe(viewLifecycleOwner) {
            when(it) {
                is PersonalViewModel.Action.MileageDeleted -> {
                    onMileageDeleted()
                }
            }
        }

        viewModel.getCurrentMileage()
    }

    override fun stateManagedViewModels(): List<BaseViewModel>? {
        return arrayListOf(viewModel)
    }

    override fun showEmptyState() {
        with(binding) {
            rvMileages.isVisible = false
            tvEmptyState.isVisible = true
        }
    }

    private fun onMileagesLoaded(expenses: List<Mileage>) {
        with(binding) {
            rvMileages.isVisible = true
            tvEmptyState.isVisible = false
        }

        adapter.submitList(expenses)
    }

    private fun onMileageDeleted() {
        toast(getString(R.string.PROFILE_MILEAGE_DELETED_SUCCESS))
        viewModel.getCurrentMileage()
    }

    private fun deleteMileage(mileage: Mileage) {
        showConfirm(R.string.PROFILE_MILEAGE_DELETION_QUESTION, { p0, p1 ->
            if(p1 == android.content.DialogInterface.BUTTON_POSITIVE) {
                viewModel.deleteMileage(mileage)
            }
        })
    }

    private fun navigateToAddMileage() {
        val action = PersonalMileageListFragmentDirections.actionPersonalMileageListFragmentToPersonalAddMileageFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }
}