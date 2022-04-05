package com.gesecur.app.ui.operator.workorder.material

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorAddMaterialBinding
import com.gesecur.app.domain.models.Product
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.dialog.material.SearchProductDialog
import com.gesecur.app.ui.common.toolbar.GesecurModalToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.utils.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = false)
class OperatorAddMaterialFragment : BaseFragment(R.layout.fragment_operator_add_material) {

    private val binding by viewBinding(FragmentOperatorAddMaterialBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()

    val args: OperatorAddMaterialFragmentArgs by navArgs()

    //private lateinit var notCodifiedJob : CodifiedJob

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        //notCodifiedJob = CodifiedJob(-1L, getString(R.string.ADD_JOB_NOT_CODIFIED_SELECTOR_TEXT))
//    }

    override fun setupViews() {

        with(binding) {
            btnTypeSelector.setOnClickListener {
                showProducts()
            }

            toolbarModal.isVisible = true
            toolbarModal.type = GesecurModalToolbar.Type.TITLE
            toolbarModal.setTitle(R.string.ADD_MATERIAL_TITLE)
            toolbarModal.setOptionButton(R.string.ADD_MATERIAL_SAVE_BUTTON, unit = {
                saveMaterial()
            })

            toolbarModal.setCloseAction { popBack() }

            tvQuantity.text = "1"
            btnMin.setOnClickListener {
                val quantity = tvQuantity.text.toString().toInt()

                if(quantity > 0) {
                    tvQuantity.text = (quantity - 1).toString()
                }
            }

            btnPlus.setOnClickListener {
                val quantity = tvQuantity.text.toString().toInt()

                tvQuantity.text = (quantity + 1).toString()
            }
        }
    }

    override fun setupViewModels() {
        if(viewModel.availableProducts.value == null)
            viewModel.getAvailableProducts()

        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is OperatorViewModel.Action.MaterialUpdated -> onMaterialUpdated()
            }
        })
    }

    override fun stateManagedViewModels(): List<BaseViewModel> {
        return arrayListOf(viewModel)
    }

    private fun showProducts() {
        SearchProductDialog(requireContext(), viewModel.availableProducts.value!!)
        {
            with(binding.editMaterialType) {
                tag = it
                setText(it.description)
            } }.show()
    }

    private fun saveMaterial() {
        with(binding) {
            if(isValidForm())
                viewModel.addMaterial(args.workOrderId, editMaterialType.tag as Product, tvQuantity.text.toString().toInt())
            else
                toast(R.string.ERROR_FORM_VALIDATION)
        }

    }

    private fun isValidForm(): Boolean {
        with(binding) {
            (editMaterialType.tag as? Product)?.let {
              return true //TODO check if we need to check description field
            } ?: return false
        }
    }

    private fun popBack() {
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.popBackStack()
    }

    private fun onMaterialUpdated() {
        popBack()
    }
}