package com.gesecur.app.ui.profile.expense

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentPersonalExpensesBinding
import com.gesecur.app.domain.models.Expense
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.profile.PersonalViewModel
import com.gesecur.app.ui.profile.UserProfileFragmentDirections
import com.gesecur.app.utils.showConfirm
import com.gesecur.app.utils.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = true,
    homeButton = GesecurToolbar.HomeButton.BACK,
    titleRes = R.string.PROFILE_EXPENSES)
class PersonalExpensesListFragment : BaseFragment(R.layout.fragment_personal_expenses) {

    private val binding by viewBinding(FragmentPersonalExpensesBinding::bind)
    private val viewModel by sharedViewModel<PersonalViewModel>()

    private val adapter = PersonalExpensesAdapter()

    override fun setupViews() {

        with(binding) {
            adapter.onItemDeleteClick = { expense ->
               deleteExpense(expense)
            }

            rvExpenses.adapter = adapter
            rvExpenses.layoutManager = LinearLayoutManager(requireContext())

            btnAdd.setOnClickListener { navigateToAddMileage() }
        }
    }

    override fun setupViewModels() {
        viewModel.expenses.observe(viewLifecycleOwner, {
            onExpensesLoaded(it)
        })

        viewModel.viewAction.observe(viewLifecycleOwner) {
            when(it) {
                is PersonalViewModel.Action.ExpenseDeleted -> {
                    onExpenseDeleted()
                }
            }
        }

        viewModel.getCurrentExpenses()
    }

    override fun stateManagedViewModels(): List<BaseViewModel>? {
        return arrayListOf(viewModel)
    }

    private fun onExpensesLoaded(expenses: List<Expense>) {
        with(binding) {
            rvExpenses.isVisible = true
            tvEmptyState.isVisible = false
        }
        adapter.submitList(expenses)
    }

    override fun showEmptyState() {
        with(binding) {
            rvExpenses.isVisible = false
            tvEmptyState.isVisible = true
        }
    }

    private fun onExpenseDeleted() {
        toast(getString(R.string.PROFILE_EXPENSE_DELETED_SUCCESS))
        viewModel.getCurrentExpenses()
    }

    private fun deleteExpense(expense: Expense) {
        showConfirm(R.string.PROFILE_EXPENSE_DELETION_QUESTION, { p0, p1 ->
            if(p1 == android.content.DialogInterface.BUTTON_POSITIVE) {
                viewModel.deleteExpense(expense)
            }
        })
    }

    private fun navigateToAddMileage() {
        val action = PersonalExpensesListFragmentDirections.actionPersonalExpensesListFragmentToPersonalAddExpenseFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }
}