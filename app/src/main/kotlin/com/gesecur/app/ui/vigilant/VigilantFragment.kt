package com.gesecur.app.ui.vigilant

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.BuildConfig
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorBinding
import com.gesecur.app.databinding.FragmentVigilantBinding
import com.gesecur.app.ui.common.base.BaseActivity
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.incidences.IncidencesViewModel
import com.gesecur.app.ui.operator.OperatorFragmentDirections
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.utils.DateUtils
import com.gesecur.app.utils.getCurrentLocation
import com.gesecur.app.utils.showConfirm
import com.gesecur.app.utils.toToolbarFormat
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

@ToolbarOptions(
    showToolbar = true)
class VigilantFragment : BaseFragment(R.layout.fragment_vigilant) {

    private val binding by viewBinding(FragmentVigilantBinding::bind)
    private val viewModel by sharedViewModel<VigilantViewModel>()
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

        (requireActivity() as VigilantActivity).setToolbarExtra(R.drawable.ic_exit) {
            showConfirm(R.string.VIGILANT_EXIT_QUESTION, { p0, p1 ->
                if(p1 == android.content.DialogInterface.BUTTON_POSITIVE) {
                    viewModel.currentTurn.value?.let {
                        requireActivity().getCurrentLocation {
                            it?.let { viewModel.endTurn(viewModel.currentTurn.value!!.id, it.latitude, it.longitude) }
                        }
                    } ?: run {
                        viewModel.closeSession()
                        navigateToSplash()
                    }

                }
            })
        }
    }

    private fun navigateOnTabSelected(tab: TabLayout.Tab?) {
        with(childFragmentManager.findFragmentById(R.id.nav_vigilant_tab) as NavHostFragment) {
            val navController = navController

            navController.setGraph(R.navigation.nav_vigilant)
            navController.navigate(
                if(tab?.position == 0) R.id.nav_vigilant_news
                else R.id.nav_vigilant_incidences)
        }
    }

    override fun setupViewModels() {
        viewModel.mainViewAction.observe(this) {
            when (it) {
                is VigilantViewModel.MainAction.NavigateToRegistries -> navigateToRegistries()
                is VigilantViewModel.MainAction.NavigateToObservations -> navigateToObservations()

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

    private fun navigateToRegistries() {
        viewModel.currentTurn.value?.let {
            val action = VigilantFragmentDirections.actionVigilantFragmentToVigilantRegistryListFragment(
                it.id
            )

            parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
        }
    }

    private fun navigateToObservations() {
        viewModel.currentTurn.value?.let {
            val action = VigilantFragmentDirections.actionVigilantFragmentToVigilantObservationsListFragment(
                it.id
            )

            parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
        }
    }

    private fun navigateToAddIncidence() {
        val action = VigilantFragmentDirections.actionVigilantFragmentToIncidencesAddIncidenceFragment2()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }
}