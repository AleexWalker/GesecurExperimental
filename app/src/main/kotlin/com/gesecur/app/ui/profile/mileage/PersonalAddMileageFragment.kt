package com.gesecur.app.ui.profile.mileage

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentPersonalAddMileageBinding
import com.gesecur.app.domain.models.Mileage
import com.gesecur.app.domain.models.Vehicle
import com.gesecur.app.domain.models.WorkPart
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
class PersonalAddMileageFragment : BaseFileFragment(R.layout.fragment_personal_add_mileage) {

    private val binding by viewBinding(FragmentPersonalAddMileageBinding::bind)
    private val viewModel by viewModel<PersonalViewModel>()
    private val partViewModel by viewModel<OperatorViewModel>()


    private lateinit var ownVehicle : Vehicle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ownVehicle = Vehicle(-1L, "", "", "")
    }

    override fun setupViews() {

        with(binding) {
            btnTypeSelector.setOnClickListener {
                showVehiclesOptions()
            }

            btnPartSelector.setOnClickListener {
                showPartsOptions()
            }

            toolbarModal.isVisible = true
            toolbarModal.type = GesecurModalToolbar.Type.TITLE
            toolbarModal.setTitle(R.string.PROFILE_MILEAGE_ADD_MILEAGE_TITLE)
            toolbarModal.setOptionButton(R.string.PROFILE_MILEAGE_ADD_SAVE_BUTTON, unit = {
                saveMileage()
            })

            toolbarModal.setCloseAction { popBack() }

            btnFiles.setOnClickListener { onAddFileClicked() }
        }
    }

    override fun setupViewModels() {
        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is PersonalViewModel.Action.MileageAdded -> onMileageAdded()
                is BaseAction.ShowError -> {
                    toast(
                        it.error
                    )
                }
            }
        })

        viewModel.getVehicles()
        partViewModel.selectDateForPart(LocalDate.now())
    }

    override fun stateManagedViewModels(): List<BaseViewModel> {
        return arrayListOf(viewModel, partViewModel)
    }

    private fun showVehiclesOptions() {
        val data = viewModel.vehicles.value?.toMutableList() ?: arrayListOf()
        data.add(ownVehicle)

        val dialog: OptionsDialog<Vehicle> = OptionsDialog(requireContext(), data, binding.editVehicle.tag as? Vehicle,
                object : OptionsDialog.DialogSelectHandler<Vehicle> {
                    override fun onSelect(data: Vehicle?, position: Int) {
                        if (data != null) {

                            with(binding.editVehicle) {
                                tag = data
                                setText(
                                    if(data.id == -1L) getString(R.string.PROFILE_MILEAGE_ADD_OWN_VEHICLE) else data.toString()
                                )
                            }
                        }
                    }
                },
                object: OptionsDialog.OptionsDialogRenderer<Vehicle> {
                    override fun onRender(data: Vehicle): String {
                        return if(data.id == -1L) getString(R.string.PROFILE_MILEAGE_ADD_OWN_VEHICLE) else data.toString()
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

    private fun saveMileage() {
        with(binding) {
            if(isValidForm())
                viewModel.addMileage(
                    Mileage(-1,
                        (editPart.tag as? WorkPart)?.id ?: 0,
                        null,
                        null,
                        null,
                    null,
                        editDesc.text.toString(),
                        editKmIni.text.toString().toDouble(),
                        editKm.text.toString().toDouble(),
                        "",
                        LocalDateTime.now()),
                    editVehicle.tag as Vehicle,
                    fileSelected)
            else
                toast(R.string.ERROR_FORM_VALIDATION)
        }

    }

    private fun isValidForm(): Boolean {
        return with(binding) {
            editVehicle.tag != null && !editDesc.text.isNullOrEmpty()
                    && !editKmIni.text.isNullOrEmpty()
                    && !editKm.text.isNullOrEmpty()
                    && fileSelected != null
        }
    }

    private fun popBack() {
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.popBackStack()
    }

    private fun onMileageAdded() {
        toast(getString(R.string.PROFILE_MILEAGE_ADD_MILEAGE_SUCCESS))
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