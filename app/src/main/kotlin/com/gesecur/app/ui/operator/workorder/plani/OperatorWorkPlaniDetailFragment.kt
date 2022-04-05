package com.gesecur.app.ui.operator.workorder.plani

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorWorkplaniDetailBinding
import com.gesecur.app.domain.models.WorkPart
import com.gesecur.app.domain.models.WorkPlanification
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorActivity
import com.gesecur.app.ui.operator.OperatorFragmentDirections
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.ui.operator.workorder.parts.PersonalAdapter
import com.gesecur.app.utils.DateUtils
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = true,
    titleRes = R.string.WORK_PLANIFICATION_TITLE,
    homeButton = GesecurToolbar.HomeButton.BACK)
class OperatorWorkPlaniDetailFragment : BaseFragment(R.layout.fragment_operator_workplani_detail) {

    companion object {
        const val TAB_JOB = 0
        const val TAB_MATERIAL = 1
        const val TAB_OTHERS = 2
        const val TAB_PERSONAL = 3
    }

    private val binding by viewBinding(FragmentOperatorWorkplaniDetailBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()

    val args: OperatorWorkPlaniDetailFragmentArgs by navArgs()

    private val jobsAdapter = PlanificationInternalAdapter()
    private val materialsAdapter = PlanificationInternalAdapter()
    private val othersAdapter = PlanificationInternalAdapter()
    private val personalAdapter = PersonalAdapter()

    override fun setupViews() {

        with(binding) {

            tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    with(tab?.position == TAB_JOB) {
                        rvJobs.isVisible = tab?.position == TAB_JOB
                        rvMaterials.isVisible = tab?.position == TAB_MATERIAL
                        rvOther.isVisible = tab?.position == TAB_OTHERS
                        rvPersonal.isVisible = tab?.position == TAB_PERSONAL
                    }
                }
            })

            rvJobs.adapter = jobsAdapter
            rvJobs.layoutManager = object: LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

            rvMaterials.adapter = materialsAdapter
            rvMaterials.layoutManager = object: LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

            rvOther.adapter = othersAdapter
            rvOther.layoutManager = object: LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

            rvPersonal.adapter = personalAdapter
            rvPersonal.layoutManager = object: LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

            btnInitOrder.setOnClickListener {
                startPlanification()
            }

            (requireActivity() as OperatorActivity).setToolbarExtra(R.drawable.ic_account) {
                navigateToUserProfile()
            }
        }
    }

    override fun setupViewModels() {
        viewModel.workPlaniDetail.observe(viewLifecycleOwner, { onWorkPlaniLoaded(it) })
        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is BaseAction.ShowError ->  {
                    showError(it.error)
                }

                is OperatorViewModel.Action.OnPlanificationStarted -> {
                    //setPlanificationStarted()

                    viewModel.getWorkPlaniDetail(args.workPlaniId, DateUtils.localDateFromLong(args.date))
                }
            }
        })

        viewModel.getWorkPlaniDetail(args.workPlaniId, DateUtils.localDateFromLong(args.date))

    }

    override fun stateManagedViewModels() = arrayListOf(viewModel)

    private fun onWorkPlaniLoaded(workPlani: WorkPlanification) {
        with(binding) {
            tvIdentifier.apply {
                text = getString(R.string.WORK_PLANI_IDENTIFIER, workPlani.planiId?.toString() ?: "X")
                setTextColor(Color.parseColor(workPlani.stateColor))
            }

            tvDate.text = workPlani.getDateWithHours()
            tvOrderDesc.text = workPlani.desc
            tvOrderLocalization.text = workPlani.getCompleteAddress()

            jobsAdapter.submitList(workPlani.jobs.map { PlanificationInternalAdapter.PlanificationInternalItem(
                it.description, it.quantity, it.otQuantity - it.notAvailableQty)
            })
            materialsAdapter.submitList(workPlani.materials.map { PlanificationInternalAdapter.PlanificationInternalItem(
                it.name ?: it.concept ?: "", it.quantity, it.otQuantity - it.notAvailableQty)
            })
            othersAdapter.submitList(workPlani.others.map { PlanificationInternalAdapter.PlanificationInternalItem(
                it.description, it.quantity, it.otQuantity - it.notAvailableQty)
            })
            personalAdapter.submitList(workPlani.personal)

            if(!workPlani.hasStartedPlanificationForDate(DateUtils.localDateFromLong(args.date)))
                setPlanificationNotStarted()
            else {
                setPlanificationStarted()

                btnSeePart.setOnClickListener {
                    workPlani.dateIni?.let {
                            workPlani.getPartByDate(it.toLocalDate())
                                ?.let { part -> navigateToPart(workPart = part) }
                    }
                }
            }
        }
    }

    private fun setPlanificationStarted() {
        with(binding) {
            bottomContainer.isVisible = true
            btnSeePart.isVisible = true
            btnInitOrder.isVisible = false
        }
    }

    private fun setPlanificationNotStarted() {
        with(binding) {
            bottomContainer.isVisible = true
            btnSeePart.isVisible = false
            btnInitOrder.isVisible = true
        }
    }

    private fun startPlanification() {
        viewModel.startPlanification(args.workPlaniId)
    }

    private fun navigateToUserProfile() {
        val action = OperatorFragmentDirections.actionOperatorFragmentToUserProfileFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToPart(workPart: WorkPart) {
        val action = OperatorWorkPlaniDetailFragmentDirections
                        .actionOperatorWorkPlaniDetailFragmentToOperatorWorkOrderDetailFragment(
                                workPartId = workPart.id ?: -1L,
                                editable = !workPart.isPastPart())

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

}