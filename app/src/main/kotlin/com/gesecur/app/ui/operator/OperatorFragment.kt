package com.gesecur.app.ui.operator

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorBinding
import com.gesecur.app.domain.models.WorkPart
import com.gesecur.app.domain.models.WorkPlanification
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.incidences.IncidencesViewModel
import com.gesecur.app.ui.vigilant.VigilantActivity
import com.gesecur.app.utils.showConfirm
import com.gesecur.app.utils.toToolbarFormat
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@ToolbarOptions(
    showToolbar = true)
class OperatorFragment : BaseFragment(R.layout.fragment_operator) {

    private val binding by viewBinding(FragmentOperatorBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()
    private val incidenceViewModel by sharedViewModel<IncidencesViewModel>()

    private var selectedTabPosition = 0

    override fun setupViews() {
        setTitle(title = LocalDate.now().toToolbarFormat())

        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                navigateOnTabSelected(tab)
                selectedTabPosition = tab?.position ?: 0
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                navigateOnTabSelected(tab)
                selectedTabPosition = tab?.position ?: 0
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        binding.tablayout.getTabAt(selectedTabPosition)?.select()

        (requireActivity() as OperatorActivity).setToolbarExtra(R.drawable.ic_account) {
            navigateToUserProfile()
        }
    }

    private fun navigateOnTabSelected(tab: TabLayout.Tab?) {
        with(childFragmentManager.findFragmentById(R.id.nav_operator_tab) as NavHostFragment) {
            val navController = navController

            navController.setGraph(R.navigation.nav_operator)
            navController.navigate(
                when(tab?.position) {
                    0 -> R.id.nav_operator_management_plani
                    1 -> R.id.nav_operator_management_parts
                    else -> R.id.nav_operator_incidences
                })
        }
    }

    override fun setupViewModels() {
        viewModel.viewAction.observe(this) {
            when (it) {
                is OperatorViewModel.Action.OnWorkOrderSelected -> onPartSelected(it.workPart)
                is OperatorViewModel.Action.OnWorkPlaniSelected -> onPlaniSelected(it.workPlanification)
                is OperatorViewModel.Action.OnPlanificationStarted -> navigateToOrderDetail(it.partId, true)
            }
        }

        incidenceViewModel.mainViewAction.observe(viewLifecycleOwner, {
            when(it) {
                is IncidencesViewModel.MainAction.navigateToAddIncidence -> {
                    navigateToAddIncidence()
                }
            }
        })
    }

    private fun onPartSelected(workPart: WorkPart) {
        workPart.id?.let {
            navigateToOrderDetail(it, !workPart.isPastPart())
        } ?: run {
            //viewModel.startPlanification(workPart.planiId!!)
        }
    }

    private fun onPlaniSelected(workPlani: WorkPlanification) {
        workPlani.planiId?.let {
            navigateToPlanificationDetail(it, (workPlani.dateIni ?: LocalDateTime.now()).atZone(ZoneId.systemDefault()).toEpochSecond() )
        } ?: run {
            //viewModel.startPlanification(workPart.planiId!!)
        }
    }

    private fun navigateToOrderDetail(workPartId: Long, editable: Boolean) {
        val action = OperatorFragmentDirections.actionOperatorFragmentToOperatorWorkOrderDetailFragment(
            workPartId,
            editable
        )

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToPlanificationDetail(workPlaniId: Long, date: Long) {
        val action = OperatorFragmentDirections.actionOperatorFragmentToOperatorWorkPlaniDetailFragment(
            workPlaniId,
            date
        )

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToAddIncidence() {
        val action = OperatorFragmentDirections.actionOperatorFragmentToIncidencesAddIncidenceFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToUserProfile() {
        val action = OperatorFragmentDirections.actionOperatorFragmentToUserProfileFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }
}