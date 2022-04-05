package com.gesecur.app.ui.operator.workorder.plani

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorWorkplaniListBinding
import com.gesecur.app.domain.models.WorkPlanification
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = true)
class OperatorWorkPlaniListFragment : BaseFragment(R.layout.fragment_operator_workplani_list) {

    private val binding by viewBinding(FragmentOperatorWorkplaniListBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()

    private val workPlanisAdapter = WorkPlanisAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.workPlanis.value?.let {
            onWorkPlanisLoaded(it)
        }
    }

    override fun setupViews() {

        with(binding) {

            workPlanisAdapter.onItemClick = { workPlani ->
               viewModel.selectWorkPlani(workPlani)
            }

            rvWorkPlanis.adapter = workPlanisAdapter
            rvWorkPlanis.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setupViewModels() {
        viewModel.workPlanis.observe(viewLifecycleOwner, {
            onWorkPlanisLoaded(it)
        })
    }

    override fun stateManagedViewModels(): List<BaseViewModel>? {
        return arrayListOf(viewModel)
    }

    override fun showEmptyState() {
        with(binding) {
            rvWorkPlanis.isVisible = false
            tvEmpty.isVisible = true
        }
    }

    override fun showSuccessState() {
        with(binding) {
            rvWorkPlanis.isVisible = true
            tvEmpty.isVisible = false
        }
    }

    private fun onWorkPlanisLoaded(workPlanifications: List<WorkPlanification>) {
        workPlanisAdapter.submitList(workPlanifications)
    }
}