package com.gesecur.app.ui.incidences

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.location.Address
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toIcon
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentAddIncidenceBinding
import com.gesecur.app.domain.models.Incidence
import com.gesecur.app.domain.models.IncidenceType
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseFileFragment
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.dialog.OptionsDialog
import com.gesecur.app.ui.common.toolbar.GesecurModalToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.utils.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.time.LocalDateTime

@ToolbarOptions(
    showToolbar = false)
class IncidencesAddIncidenceFragment : BaseFileFragment(R.layout.fragment_add_incidence) {

    private val binding by viewBinding(FragmentAddIncidenceBinding::bind)
    private val viewModel by sharedViewModel<IncidencesViewModel>()

    private lateinit var address: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().getCurrentLocation {

            it?.let { it1 ->
                onLocationObtained(it1.latitude, it1.longitude)
            }
        }

        super.onCreate(savedInstanceState)
    }

    override fun setupViews() {

        with(binding) {
            btnTypeSelector.setOnClickListener {
                showTypeOptions()
            }

            toolbarModal.isVisible = true
            toolbarModal.type = GesecurModalToolbar.Type.TITLE
            toolbarModal.setTitle(R.string.ADD_INCIDENCE_TITLE)
            toolbarModal.setOptionButton(R.string.ADD_INCIDENCE_SAVE_BUTTON, unit = {
                saveIncidence()
            })

            toolbarModal.setCloseAction { popBack() }

            btnChangeLocalization.setOnClickListener { navigateToModifyAddress() }

            if(::address.isInitialized)
                printLocation(GeoCoderUtil.getAdressAsString(address))

            btnFiles.setOnClickListener { onAddClicked() }
        }
    }

    private fun onLocationObtained(lat: Double, lon: Double) {
        GeoCoderUtil.execute(requireContext(), lat, lon) { address, addressString ->
            this.address = address

            printLocation(addressString)
        }
    }

    override fun setupViewModels() {
        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is IncidencesViewModel.Action.onLocationChanged -> {
                    onLocationObtained(it.lat, it.lon)
                }

                is IncidencesViewModel.Action.onIncidenceAdded -> {
                    onIncidenceAdded()
                }

                is BaseAction.ShowError -> {
                    toast(
                        it.error
                    )
                }
            }
        })
    }

    override fun stateManagedViewModels(): List<BaseViewModel> {
        return arrayListOf(viewModel)
    }

    private fun printLocation(address: String) {
        binding.tvLocation.text = address
    }

    private fun showTypeOptions() {
        val dialog: OptionsDialog<IncidenceType> = OptionsDialog(requireContext(), viewModel.incidenceTypes.value!!, binding.editIncidenceType.tag as? IncidenceType,
                object : OptionsDialog.DialogSelectHandler<IncidenceType> {
                    override fun onSelect(data: IncidenceType?, position: Int) {
                        if (data != null) {

                            with(binding.editIncidenceType) {
                                tag = data
                                setText(data.description)
                            }
                        }
                    }
                },
                object: OptionsDialog.OptionsDialogRenderer<IncidenceType> {
                    override fun onRender(data: IncidenceType): String {
                        return data.description
                    }
                })

        dialog.show()
    }

    private fun saveIncidence() {
        with(binding) {
            if(isValidForm())
                viewModel.addIncidence(
                    Incidence(-1,
                        (editIncidenceType.tag as? IncidenceType)!!.id,
                        "",
                        binding.tvLocation.text.toString(),
                        address.latitude, address.longitude,
                        editIncidenceDesc.text.toString(), LocalDateTime.now(), null
                    ), fileSelected
                )
            else
                toast(R.string.ERROR_FORM_VALIDATION)
        }
    }

    private fun isValidForm(): Boolean {
        with(binding) {
            (editIncidenceType.tag as? IncidenceType)?.let {
              return !editIncidenceDesc.text.isNullOrEmpty() && address != null
            } ?: return false
        }
    }

    private fun navigateToModifyAddress() {
        val action = IncidencesAddIncidenceFragmentDirections.actionIncidencesAddIncidenceFragmentToIncidenceMapFragment(
            address.latitude.toFloat(),
            address.longitude.toFloat())

        parentFragmentManager.primaryNavigationFragment?.findNavController()?.navigate(action)
    }

    private fun onAddClicked() {
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

    private fun popBack() {
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.popBackStack()
    }

    private fun onIncidenceAdded() {
        toast(R.string.ADD_INCIDENCE_SUCCESS)
        popBack()
    }
}