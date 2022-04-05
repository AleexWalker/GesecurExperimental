package com.gesecur.app.ui.operator.workorder.job

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorAddJobBinding
import com.gesecur.app.domain.models.CodifiedJob
import com.gesecur.app.domain.models.Service
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.dialog.OptionsDialog
import com.gesecur.app.ui.common.toolbar.GesecurModalToolbar
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.utils.getIntValue
import com.gesecur.app.utils.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = false)
class OperatorAddJobFragment : BaseFragment(R.layout.fragment_operator_add_job) {

    private val binding by viewBinding(FragmentOperatorAddJobBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()

    val args: OperatorAddJobFragmentArgs by navArgs()

    private lateinit var notCodifiedJob : CodifiedJob

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notCodifiedJob = CodifiedJob(-1L, getString(R.string.ADD_JOB_NOT_CODIFIED_SELECTOR_TEXT))
    }

    override fun setupViews() {

        with(binding) {
            btnTypeSelector.setOnClickListener {
                showTypeOptions()
            }

            toolbarModal.isVisible = true
            toolbarModal.type = GesecurModalToolbar.Type.TITLE
            toolbarModal.setTitle(R.string.ADD_JOB_TITLE)
            toolbarModal.setOptionButton(R.string.ADD_JOB_SAVE_BUTTON, unit = {
                saveJob()
            })

            toolbarModal.setCloseAction { popBack() }
        }
    }

    override fun setupViewModels() {
        if(viewModel.codifiedJobs.value == null)
            viewModel.getCodifiedJobs()

        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is OperatorViewModel.Action.JobAdded -> onJobAdded()
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

    private fun showTypeOptions() {
        val data = viewModel.codifiedJobs.value!!.toMutableList()
        data.add(notCodifiedJob)

        val dialog: OptionsDialog<CodifiedJob> = OptionsDialog(requireContext(), data, binding.editJobType.tag as? CodifiedJob,
                object : OptionsDialog.DialogSelectHandler<CodifiedJob> {
                    override fun onSelect(data: CodifiedJob?, position: Int) {
                        if (data != null) {

                            with(binding.editJobType) {
                                tag = data
                                setText(data.description)

                                with(data == notCodifiedJob) {
                                    binding.inputJobDescription.isEnabled = this
                                    binding.inputJobDuration.isEnabled = this

                                    if(!this) {
                                        binding.editJobDesc.setText("")
                                        binding.editJobDuration.setText("")
                                    }
                                }
                            }
                        }
                    }
                },
                object: OptionsDialog.OptionsDialogRenderer<CodifiedJob> {
                    override fun onRender(data: CodifiedJob): String {
                        return data.description
                    }
                })

        dialog.show()
    }

    private fun saveJob() {
        with(binding) {
            if(isValidForm())
                viewModel.addJob(args.workpartId, editJobType.tag as CodifiedJob,
                    editJobDesc.text.toString(),
                    editJobDuration.getIntValue())
            else
                toast(R.string.ERROR_FORM_VALIDATION)
        }

    }

    private fun isValidForm(): Boolean {
        with(binding) {
            (editJobType.tag as? CodifiedJob)?.let {
              return it.isReallyCodified() == 1 ||
                      (!editJobDesc.text.isNullOrBlank() && !editJobDuration.text.isNullOrBlank())
            } ?: return false
        }
    }

    private fun popBack() {
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.popBackStack()
    }

    private fun onJobAdded() {
        popBack()
    }
}