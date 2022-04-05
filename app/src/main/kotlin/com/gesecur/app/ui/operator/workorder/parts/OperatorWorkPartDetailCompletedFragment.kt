package com.gesecur.app.ui.operator.workorder.parts

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorWorkpartDetailCompletedBinding
import com.gesecur.app.domain.models.WorkPart
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.dialog.FinishOperatorWorkClientSignDialog
import com.gesecur.app.ui.common.dialog.FinishOperatorWorkDialog
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorActivity
import com.gesecur.app.ui.operator.OperatorFragmentDirections
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.ui.operator.workorder.job.JobsAdapter
import com.gesecur.app.ui.operator.workorder.material.WorkMaterialsAdapter
import com.gesecur.app.ui.operator.workorder.other.OtherAdapter
import com.gesecur.app.utils.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = true,
    titleRes = R.string.WORK_ORDER_TITLE,
    homeButton = GesecurToolbar.HomeButton.BACK)
class OperatorWorkPartDetailCompletedFragment : BaseFragment(R.layout.fragment_operator_workpart_detail_completed) {

    private val binding by viewBinding(FragmentOperatorWorkpartDetailCompletedBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()

    val args: OperatorWorkPartDetailFragmentArgs by navArgs()

    private val jobsAdapter = JobsAdapter()
    private val materialsAdapter = WorkMaterialsAdapter()
    private val othersAdapter = OtherAdapter()

    override fun setupViews() {

        with(binding) {

            jobsAdapter.editable = false
            rvJobs.adapter = jobsAdapter
            rvJobs.layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

            materialsAdapter.editable = false
            rvMaterials.adapter = materialsAdapter
            rvMaterials.layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

            othersAdapter.editable = false
            rvOther.adapter = othersAdapter
            rvOther.layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

            btnConfirm.setOnClickListener {
                FinishOperatorWorkClientSignDialog(requireContext()) { dni, obs, isFault ->
                    closePartWork(dni, obs, isFault)
                }.show()
            }

            (requireActivity() as OperatorActivity).setToolbarExtra(R.drawable.ic_account) {
                navigateToUserProfile()
            }
        }
    }

    override fun setupViewModels() {
        viewModel.workPartDetail.observe(viewLifecycleOwner, { onWorkOrderLoaded(it) })
        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is BaseAction.ShowError ->  {
                    showError(it.error)
                }

                is OperatorViewModel.Action.PartCloseSuccess -> {
                    onPartClosedSuccessfully()
                }
            }
        })

        viewModel.getWorkPartDetail(args.workPartId)
    }

    override fun stateManagedViewModels() = arrayListOf(viewModel)

    private fun onWorkOrderLoaded(workPart: WorkPart) {
        with(binding) {
            tvIdentifier.apply {
                text = getString(R.string.WORK_PART_IDENTIFIER, workPart.id?.toString() ?: "X")
                setTextColor(Color.parseColor(workPart.stateColor))
            }

            tvDate.text = workPart.getDateWithHours()
            tvOrderDesc.text = workPart.desc
            tvOrderLocalization.text = workPart.address

            with(workPart.jobs) {
                jobsAdapter.submitList(workPart.jobs)

                rvJobs.isVisible = isNotEmpty()
                tvJobsEmpty.isVisible = isEmpty()
            }

            with(workPart.materials) {
                materialsAdapter.submitList(this)

                rvMaterials.isVisible = isNotEmpty()
                tvMaterialEmpty.isVisible = isEmpty()
            }

            with(workPart.others) {
                othersAdapter.submitList(this)

                rvOther.isVisible = isNotEmpty()
                tvOtherEmpty.isVisible = isEmpty()
            }
        }
    }

    private fun closePartWork(dni: String, obs: String, faults: Boolean) {
        viewModel.closePart(viewModel.workPartDetail.value!!, dni, obs, faults)
    }

    private fun navigateToUserProfile() {
        val action = OperatorFragmentDirections.actionOperatorFragmentToUserProfileFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun onPartClosedSuccessfully() {
        toast(getString(R.string.OPERATION_FINISH_WORK_PART_CLOSED_SUCCESS))

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.popBackStack(R.id.operatorFragment, false)

    }
}