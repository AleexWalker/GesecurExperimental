package com.gesecur.app.ui.operator.workorder.parts

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorWorkpartDetailBinding
import com.gesecur.app.domain.models.*
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.dialog.FinishOperatorWorkDialog
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorActivity
import com.gesecur.app.ui.operator.OperatorFragmentDirections
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.ui.operator.workorder.attachment.AttachmentsAdapter
import com.gesecur.app.ui.operator.workorder.job.JobsAdapter
import com.gesecur.app.ui.operator.workorder.material.WorkMaterialsAdapter
import com.gesecur.app.ui.operator.workorder.other.OtherAdapter
import com.gesecur.app.utils.*
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@ToolbarOptions(
    showToolbar = true,
    titleRes = R.string.WORK_ORDER_TITLE,
    homeButton = GesecurToolbar.HomeButton.BACK)
class OperatorWorkPartDetailFragment : BaseFragment(R.layout.fragment_operator_workpart_detail) {

    companion object {
        const val TAB_JOB = 0
        const val TAB_MATERIAL = 1
        const val TAB_OTHERS = 2
        const val TAB_PERSONAL = 3
        const val TAB_ATTACHMENTS = 4
    }

    private val binding by viewBinding(FragmentOperatorWorkpartDetailBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()

    val args: OperatorWorkPartDetailFragmentArgs by navArgs()

    private val jobsAdapter = JobsAdapter()
    private val materialsAdapter = WorkMaterialsAdapter()
    private val othersAdapter = OtherAdapter()
    private val personalAdapter = PersonalAdapter()
    private val attachmentsAdapter = AttachmentsAdapter()

    override fun setupViews() {

        with(binding) {

            tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    rvJobs.isVisible = tab?.position == TAB_JOB
                    rvMaterials.isVisible = tab?.position == TAB_MATERIAL
                    rvOther.isVisible = tab?.position == TAB_OTHERS
                    rvPersonal.isVisible = tab?.position == TAB_PERSONAL
                    rvAttachments.isVisible = tab?.position == TAB_ATTACHMENTS
                }
            })

            jobsAdapter.onItemDeleteClick = { job ->
                deleteJob(job)
            }

            rvJobs.adapter = jobsAdapter
            rvJobs.layoutManager = object: LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return true
                }
            }

            materialsAdapter.onItemMinusClick = { material, quantity ->
                onMaterialChanged(material, quantity)
            }

            materialsAdapter.onItemPlusClick = { material, quantity ->
                onMaterialChanged(material, quantity)
            }

            jobsAdapter.onItemMinusClick = { job, quantity ->
                onJobQuantityChanged(job, quantity)
            }

            jobsAdapter.onItemPlusClick = { job, quantity ->
                onJobQuantityChanged(job, quantity)
            }

            rvMaterials.adapter = materialsAdapter
            rvMaterials.layoutManager = object: LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return true
                }
            }


            othersAdapter.onItemMinusClick = { other, quantity ->
                onOtherQuantityChanged(other, quantity)
            }

            othersAdapter.onItemPlusClick = { other, quantity ->
                onOtherQuantityChanged(other, quantity)
            }

            rvOther.adapter = othersAdapter
            rvOther.layoutManager = object: LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return true
                }
            }

            rvPersonal.adapter = personalAdapter
            rvPersonal.layoutManager = object: LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return true
                }
            }

            attachmentsAdapter.pdfClickCallback = {
                showPdf(it)
            }

            rvAttachments.adapter = attachmentsAdapter
            rvAttachments.layoutManager = object: LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return true
                }
            }

            fabAddJob.initOption()
            fabAddMaterial.initOption()
            fabAddAttachment.initOption()

            fabAddJob.setOnClickListener { navigateToJobAdd() }
            fabAddMaterial.setOnClickListener { navigateToMaterialAdd() }
            fabAddAttachment.setOnClickListener { navigateToAttachmentAdd() }

            fabAdd.setOnClickListener {
                if(areOptionsVisible()) fabAdd.closeOptions(arrayListOf(fabAddMaterial, fabAddJob))
                else
                    fabAdd.openOptions(arrayListOf(fabAddMaterial, fabAddJob, fabAddAttachment))
            }

            btnInitOrder.setOnClickListener {
                startJob()
            }

            btnFinishOrder.setOnClickListener {
                finishJob()
            }

            (requireActivity() as OperatorActivity).setToolbarExtra(R.drawable.ic_account) {
                navigateToUserProfile()
            }

            with(args.editable) {
                fabOptionsContainer.isVisible = this

                jobsAdapter.editable = this
                materialsAdapter.editable = this
                othersAdapter.editable = this
            }
        }
    }

    private fun areOptionsVisible() = binding.fabAddJob.isVisible || binding.fabAddMaterial.isVisible


    override fun setupViewModels() {
        viewModel.workPartDetail.observe(viewLifecycleOwner, { onWorkOrderLoaded(it) })
        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is OperatorViewModel.Action.JobDeleted -> onJobDeleted(it.message)
                is OperatorViewModel.Action.JobStarted -> onJobStarted()
                is OperatorViewModel.Action.IsJobStartedByOther -> onJobStartedByOther()
                is OperatorViewModel.Action.JobFinishedButNotCompleted -> onJobFinishedButNotCompleted()
                is OperatorViewModel.Action.JobFinished -> onJobFinished()
                is OperatorViewModel.Action.PartClosed -> onPartClosed()
                is OperatorViewModel.Action.JobStartedSeconds -> printJobStartedSeconds(it.seconds)
                is BaseAction.ShowError ->  {
                    showError(it.error)
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

            jobsAdapter.submitList(workPart.jobs)
            materialsAdapter.submitList(workPart.materials)
            othersAdapter.submitList(workPart.others)
            personalAdapter.submitList(workPart.personal)
            attachmentsAdapter.submitList(workPart.attachments)

            if(workPart.hasFinishedPart()) {
                if(workPart.clientConfirmation == 0)
                    onJobFinished()
                else
                    onJobFinishedButNotCompleted()
            }
            else {
                bottomContainer.isVisible = true
                btnInitOrder.isVisible = true
                btnClientConfirm.isVisible = false
            }

            info.setOnClickListener {
                navigateToPlanificationDetail(
                    workPart.planiId!!,
                    workPart.dateIni ?: LocalDateTime.now()
                )
            }

            maps.setOnClickListener { navigateToLocation(workPart.latitude, workPart.longitude) }
        }
    }

    private fun deleteJob(job: Job) {
        if(!viewModel.workPartDetail.value!!.hasFinishedPart()) {
            showConfirm(R.string.WORK_ORDER_DELETE_JOB_QUESTION, { p0, p1 ->
                if (p1 == DialogInterface.BUTTON_POSITIVE) {
                    viewModel.deleteJob(job)
                }
            })
        }
    }

    private fun onMaterialChanged(material: Material, quantity: Int) {
        viewModel.updateMaterial(args.workPartId, material.mapToProduct(), quantity)
    }

    private fun onOtherQuantityChanged(other: Other, quantity: Int) {
        viewModel.updateOtherQuantity(args.workPartId, other, quantity)
    }

    private fun onJobQuantityChanged(job: Job, quantity: Int) {
        viewModel.updateJobQuantity(args.workPartId, job, quantity)
    }

    private fun startJob() {
        showLoadingDialog()

        requireActivity().getCurrentLocation {
            it?.let { viewModel.startJob(viewModel.workPartDetail.value!!, it.latitude, it.longitude) }
        }
    }

    private fun finishJob() {
        FinishOperatorWorkDialog(requireContext()) { doFinishJob(it) }.show()
    }

    private fun doFinishJob(extraTime: Int) {
        showLoadingDialog()

        requireActivity().getCurrentLocation {
            it?.let { viewModel.finishJob(viewModel.workPartDetail.value!!, it.latitude, it.longitude, extraTime) }
        }
    }

    private fun onJobDeleted(message: String) {
        toast(message)
        viewModel.getWorkPartDetail(args.workPartId)
    }

    private fun onJobStartedByOther() {
        binding.tvInitiated.visibility = View.VISIBLE
    }

    private fun onJobStarted() {
        binding.bottomContainer.isVisible = true
        binding.btnInitOrder.isVisible = false
        binding.activeOrderContainer.isVisible = true
    }

    private fun onPartClosed() {
        binding.bottomContainer.isVisible = false
    }

    private fun onJobFinished() {
        //parentFragmentManager.primaryNavigationFragment?.findNavController()?.popBackStack()
        with(binding) {
            binding.bottomContainer.isVisible = true
            activeOrderContainer.isVisible = false
            btnClientConfirm.isVisible = true
            btnClientConfirm.setOnClickListener { navigateToWorkPartCompleted() }

            btnInitOrder.isVisible = false
            fabAdd.isVisible = false
            jobsAdapter.editable = false
            materialsAdapter.editable = false
            othersAdapter.editable = false
        }
    }

    private fun onJobFinishedButNotCompleted() {
        onJobFinished()
        binding.bottomContainer.isVisible = true
        binding.btnClientConfirm.isEnabled = false
    }

    private fun printJobStartedSeconds(duration: Duration) {
        binding.tvActiveOrderTimer.text = String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                duration.toHours(),
                duration.toMinutes() % 60,
                duration.seconds % 60)
    }

    private fun navigateToJobAdd() {
        val action = OperatorWorkPartDetailFragmentDirections.actionOperatorWorkOrderDetailFragmentToOperatorAddJobFragment(viewModel.workPartDetail.value?.otId ?: -1, args.workPartId)
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToMaterialAdd() {
        val action = OperatorWorkPartDetailFragmentDirections.actionOperatorWorkOrderDetailFragmentToOperatorAddMaterialFragment(args.workPartId)
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToAttachmentAdd() {
        val action = OperatorWorkPartDetailFragmentDirections.actionOperatorWorkOrderDetailFragmentToAddAttachmentFragment(args.workPartId)
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToUserProfile() {
        val action = OperatorFragmentDirections.actionOperatorFragmentToUserProfileFragment()

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToWorkPartCompleted() {
        val action = OperatorWorkPartDetailFragmentDirections.actionOperatorWorkOrderDetailFragmentToOperatorWorkPartDetailCompleteFragment(args.workPartId)

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun navigateToLocation(lat: Double, lng: Double) {
        val gmmIntentUri =
            Uri.parse("google.navigation:q=$lat, $lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun navigateToPlanificationDetail(workPlanificationId: Long, date: LocalDateTime) {
        val action = OperatorWorkPartDetailFragmentDirections
                    .actionOperatorWorkOrderDetailFragmentToOperatorWorkPlaniDetailFragment(
                            workPlaniId = workPlanificationId,
                            date = date.atZone(ZoneId.systemDefault()).toEpochSecond())

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun showPdf(pdf: String) {
        val uri = Uri.parse("https://docs.google.com/viewerng/viewer?url=$pdf")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "text/html")
        startActivity(intent)
    }
}