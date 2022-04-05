package com.gesecur.app.ui.vigilant.services

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorWorkpartListBinding
import com.gesecur.app.databinding.FragmentServicesVigilantWorkpartListBinding
import com.gesecur.app.domain.models.WorkPart
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.ui.operator.workorder.parts.WorkPartsdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = true)
class VigilantServicesWorkPartListFragment : BaseFragment(R.layout.fragment_operator_workpart_list) {

    private val binding by viewBinding(FragmentServicesVigilantWorkpartListBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()

    private val workPartsAdapter = WorkPartsdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.workParts.value?.let {
            onWorkOrdersLoaded(it)
        }
    }

    override fun setupViews() {

        with(binding) {

            workPartsAdapter.onItemClick = { workOrder ->
                viewModel.selectWorkPart(workOrder)
            }

            rvWorkOrdersServicesVigilant.adapter = workPartsAdapter
            rvWorkOrdersServicesVigilant.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setupViewModels() {
        viewModel.workParts.observe(viewLifecycleOwner, {
            onWorkOrdersLoaded(it)
        })
    }

    override fun stateManagedViewModels(): List<BaseViewModel>? {
        return arrayListOf(viewModel)
    }

    override fun showEmptyState() {
        with(binding) {
            rvWorkOrdersServicesVigilant.isVisible = false
            tvEmpty.isVisible = true
        }
    }

    override fun showSuccessState() {
        with(binding) {
            rvWorkOrdersServicesVigilant.isVisible = true
            tvEmpty.isVisible = false
        }
    }

    private fun onWorkOrdersLoaded(workPartList: List<WorkPart>) {
        workPartsAdapter.submitList(workPartList)
    }
}