package com.gesecur.app.ui.incidences

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentIncidencesListBinding
import com.gesecur.app.domain.models.Incidence
import com.gesecur.app.ui.common.arch.State
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorFragmentDirections
import com.gesecur.app.utils.toToolbarFormat
import com.gesecur.app.utils.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

@ToolbarOptions(
    showToolbar = true)
class IncidencesFragment : BaseFragment(R.layout.fragment_incidences_list) {

    private val binding by viewBinding(FragmentIncidencesListBinding::bind)
    private val viewModel by sharedViewModel<IncidencesViewModel>()

    private val adapter = IncidencesAdapter()

    override fun setupViews() {
        setTitle(title = LocalDate.now().toToolbarFormat())

        with(binding) {
            rvIncidences.layoutManager = LinearLayoutManager(requireContext())
            rvIncidences.adapter = adapter

            btnAdd.setOnClickListener { viewModel.goToAddIncidence() }
        }

    }

    override fun setupViewModels() {
        viewModel.viewAction.observe(this) {
            when (it) {

            }
        }

        viewModel.incidences.observe(viewLifecycleOwner, {
            onIncidencesLoaded(it)
        })

        viewModel.getUserIncidences(LocalDate.now())
    }

    override fun stateManagedViewModels() = arrayListOf(viewModel)

    override fun showEmptyState() {
        with(binding) {
            rvIncidences.isVisible = false
            tvEmptyState.isVisible = true
        }
    }

    private fun onIncidencesLoaded(incidencesList: List<Incidence>) {
        with(binding) {
            rvIncidences.isVisible = true
            tvEmptyState.isVisible = false
        }

        adapter.submitList(incidencesList)
    }
}