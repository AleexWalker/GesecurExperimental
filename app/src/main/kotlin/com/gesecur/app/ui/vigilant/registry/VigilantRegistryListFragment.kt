package com.gesecur.app.ui.vigilant.registry

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentNewsRegistryListBinding
import com.gesecur.app.domain.models.NewsRegistry
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.GesecurToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.vigilant.VigilantViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = true,
    titleRes = R.string.VIGILANT_REGISTRY,
    homeButton = GesecurToolbar.HomeButton.BACK)
class VigilantRegistryListFragment : BaseFragment(R.layout.fragment_news_registry_list) {

    private val binding by viewBinding(FragmentNewsRegistryListBinding::bind)
    private val viewModel by sharedViewModel<VigilantViewModel>()

    val args: VigilantRegistryListFragmentArgs by navArgs()

    private val adapter = RegistryListAdapter()

    override fun setupViews() {

        with(binding) {

            rvRegistry.adapter = adapter
            rvRegistry.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setupViewModels() {
        viewModel.newsRegistry.observe(viewLifecycleOwner, {
            onRegistriesLoaded(it)
        })

        viewModel.getNewsRegistry(args.turnId)
    }

    override fun stateManagedViewModels() = arrayListOf(viewModel)

    private fun onRegistriesLoaded(registries: List<NewsRegistry>) {
        with(registries.isNotEmpty()) {
            binding.rvRegistry.isVisible = this
            binding.tvEmpty.isVisible = !this
        }

        adapter.submitList(registries)
    }
}