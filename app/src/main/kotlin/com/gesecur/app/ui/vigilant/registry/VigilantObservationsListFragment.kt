package com.gesecur.app.ui.vigilant.registry

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentNewsObservationsListBinding
import com.gesecur.app.domain.models.NewsRegistry
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.vigilant.VigilantViewModel
import com.gesecur.app.utils.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = true,
    titleRes = R.string.VIGILANT_OBSERVATIONS,
    homeButton = GesecurToolbar.HomeButton.BACK)
class VigilantObservationsListFragment : BaseFragment(R.layout.fragment_news_observations_list) {

    private val binding by viewBinding(FragmentNewsObservationsListBinding::bind)
    private val viewModel by sharedViewModel<VigilantViewModel>()

    val args: VigilantRegistryListFragmentArgs by navArgs()

    private val adapter = ObservationsListAdapter()

    override fun setupViews() {

        with(binding) {

            rvObservations.adapter = adapter
            rvObservations.layoutManager = LinearLayoutManager(requireContext())

            btnSend.setOnClickListener {
                sendObservation()
            }
        }
    }

    override fun setupViewModels() {
        viewModel.newsObservations.observe(viewLifecycleOwner, {
            onRegistriesLoaded(it)
        })

        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is VigilantViewModel.Action.RegistrySucess -> {
                    toast(R.string.VIGILANT_NEWS_OBSERVATION_SUCCESS)
                    viewModel.getNewsObservations(args.turnId)
                    binding.editObservation.setText("")
                }
            }
        })

        viewModel.getNewsObservations(args.turnId)
    }

    override fun stateManagedViewModels() = arrayListOf(viewModel)

    private fun onRegistriesLoaded(registries: List<NewsRegistry>) {
        with(registries.isNotEmpty()) {
            binding.rvObservations.isVisible = this
            binding.tvEmpty.isVisible = !this
        }

        adapter.submitList(registries)
    }

    private fun sendObservation() {
        if(binding.editObservation.text?.isNotEmpty() == true) {
            viewModel.addNewObservations(args.turnId, binding.editObservation.text.toString())
        }
    }
}