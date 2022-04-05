package com.gesecur.app.ui.profile.expense

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentPesonalAddExpenseBinding
import com.gesecur.app.domain.models.*
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseFileFragment
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.dialog.OptionsDialog
import com.gesecur.app.ui.common.toolbar.GesecurModalToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.ui.profile.PersonalViewModel
import com.gesecur.app.utils.showConfirm
import com.gesecur.app.utils.toast
import com.gesecur.app.utils.writeToFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

@ToolbarOptions(
    showToolbar = false)
class PersonalAddExpenseFragment : BaseFileFragment(R.layout.fragment_pesonal_add_expense) {

    private val binding by viewBinding(FragmentPesonalAddExpenseBinding::bind)
    private val viewModel by viewModel<PersonalViewModel>()
    private val partViewModel by viewModel<OperatorViewModel>()


    override fun setupViews() {

        with(binding) {
            btnTypeSelector.setOnClickListener {
                showTypeOptions()
            }

            btnPartSelector.setOnClickListener {
                showPartsOptions()
            }

            toolbarModal.isVisible = true
            toolbarModal.type = GesecurModalToolbar.Type.TITLE
            toolbarModal.setTitle(R.string.PROFILE_EXPENSE_ADD_EXPENSE_TITLE)
            toolbarModal.setOptionButton(R.string.PROFILE_EXPENSE_ADD_SAVE_BUTTON, unit = {
                saveExpense()
            })

            toolbarModal.setCloseAction { popBack() }

            btnFiles.setOnClickListener { onAddFileClicked() }
        }
    }

    override fun setupViewModels() {
        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is PersonalViewModel.Action.ExpenseAdded -> onExpenseAdded()
                is BaseAction.ShowError -> {
                    toast(
                        it.error
                    )
                }
            }
        })

        viewModel.getExpenseTypes()
        partViewModel.selectDateForPart(LocalDate.now())
    }

    override fun stateManagedViewModels(): List<BaseViewModel> {
        return arrayListOf(viewModel, partViewModel)
    }

    private fun showTypeOptions() {
        val data = viewModel.expenseTypes.value!!.toMutableList()

        val dialog: OptionsDialog<ExpenseType> = OptionsDialog(requireContext(), data, binding.editType.tag as? ExpenseType,
                object : OptionsDialog.DialogSelectHandler<ExpenseType> {
                    override fun onSelect(data: ExpenseType?, position: Int) {
                        if (data != null) {

                            with(binding.editType) {
                                tag = data
                                setText(data.description)
                            }
                        }
                    }
                },
                object: OptionsDialog.OptionsDialogRenderer<ExpenseType> {
                    override fun onRender(data: ExpenseType): String {
                        return data.description
                    }
                })

        dialog.show()
    }

    private fun showPartsOptions() {
        val data = partViewModel.workParts.value?.toMutableList() ?: arrayListOf()

        val dialog: OptionsDialog<WorkPart> = OptionsDialog(requireContext(), data, binding.editPart.tag as? WorkPart,
            object : OptionsDialog.DialogSelectHandler<WorkPart> {
                override fun onSelect(data: WorkPart?, position: Int) {
                    if (data != null) {

                        with(binding.editPart) {
                            tag = data
                            setText(data.desc)
                        }
                    }
                }
            },
            object: OptionsDialog.OptionsDialogRenderer<WorkPart> {
                override fun onRender(data: WorkPart): String {
                    return data.desc ?: ""
                }
            })

        dialog.show()
    }

    private fun saveExpense() {
        with(binding) {
            if(isValidForm())
                viewModel.addExpense(
                    Expense(-1,
                        (editPart.tag as? WorkPart)?.id ?: 0,
                        null,
                        null,
                        null,
                    null,
                        editDesc.text.toString(),
                        editPrice.text.toString().toDouble(),
                        editQuantity.text.toString().toIntOrNull() ?: 1,
                            (editType.tag as ExpenseType).id,
                        null,
                        null,
                        LocalDateTime.now()),
                    fileSelected)
            else
                toast(R.string.ERROR_FORM_VALIDATION)
        }

    }

    private fun isValidForm(): Boolean {
        return with(binding) {
            editType.tag != null && !editDesc.text.isNullOrEmpty() && !editPrice.text.isNullOrEmpty() && fileSelected != null
        }
    }

    private fun popBack() {
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.popBackStack()
    }

    private fun onExpenseAdded() {
        toast(getString(R.string.PROFILE_EXPENSE_ADD_EXPENSE_SUCCESS))
        popBack()
    }

    private fun onAddFileClicked() {
        showConfirm(R.string.PROFILE_ADD_MILEAGE_EXPENSE_IMAGE_TAKE_FROM_QUESTION, {p0, p1 ->
            if (p1 == DialogInterface.BUTTON_POSITIVE) {
                checkCameraPermission()
            }
            else {
                checkFilesPermission()
            }
        },
        R.string.PROFILE_ADD_MILEAGE_EXPENSE_IMAGE_TAKE_FROM_CAMERA,
            R.string.PROFILE_ADD_MILEAGE_EXPENSE_IMAGE_TAKE_FROM_GALLERY,)
    }

    override fun onFileSelected(file: File) {
        with(binding) {
            btnFiles.isVisible = false
            tvFileName.isVisible = true
            tvFileName.text = file.name
        }
    }
}