package com.gesecur.app.ui.operator.workorder.attachment

import android.content.DialogInterface
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentPartAddAttachmentBinding
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseFileFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.toolbar.GesecurModalToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.utils.showConfirm
import com.gesecur.app.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

@ToolbarOptions(
    showToolbar = false)
class AddAttachmentFragment : BaseFileFragment(R.layout.fragment_part_add_attachment) {

    private val binding by viewBinding(FragmentPartAddAttachmentBinding::bind)
    private val partViewModel by viewModel<OperatorViewModel>()

    val args: AddAttachmentFragmentArgs by navArgs()


    override fun setupViews() {

        with(binding) {

            toolbarModal.isVisible = true
            toolbarModal.type = GesecurModalToolbar.Type.TITLE
            toolbarModal.setTitle(R.string.ADD_ATTACHMENT_TITLE)
            toolbarModal.setOptionButton(R.string.ADD_ATTACHMENT_SAVE_BUTTON, unit = {
                saveAttachment()
            })

            toolbarModal.setCloseAction { popBack() }

            btnFiles.setOnClickListener { onAddFileClicked() }
        }
    }

    override fun setupViewModels() {
        partViewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is OperatorViewModel.Action.AttachmentAdded -> onAttachmentAdded()
                is BaseAction.ShowError -> {
                    toast(
                        it.error
                    )
                }
            }
        })
    }

    override fun stateManagedViewModels(): List<BaseViewModel> {
        return arrayListOf(partViewModel)
    }

    private fun saveAttachment() {
        with(binding) {
            if(isValidForm())
                partViewModel.addAttachment(
                    args.partId,
                    editDesc.text.toString(),
                    fileSelected)
            else
                toast(R.string.ERROR_FORM_VALIDATION)
        }

    }

    private fun isValidForm(): Boolean {
        return with(binding) {
            editDesc.text != null && !editDesc.text.isNullOrEmpty()
        }
    }

    private fun popBack() {
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.popBackStack()
    }

    private fun onAttachmentAdded() {
        toast(getString(R.string.ADD_ATTACHMENT_SUCCESS))
        popBack()
    }

    private fun onAddFileClicked() {
        showConfirm(R.string.ADD_ATTACHMENT_IMAGE_TAKE_FROM_QUESTION, {p0, p1 ->
            if (p1 == DialogInterface.BUTTON_POSITIVE) {
                checkCameraPermission()
            }
            else {
                checkFilesPermission()
            }
        },
        R.string.ADD_ATTACHMENT_IMAGE_TAKE_FROM_CAMERA,
            R.string.ADD_ATTACHMENT_IMAGE_TAKE_FROM_GALLERY,)
    }

    override fun onFileSelected(file: File) {
        with(binding) {
            btnFiles.isVisible = false
            tvFileName.isVisible = true
            tvFileName.text = file.name
        }
    }
}