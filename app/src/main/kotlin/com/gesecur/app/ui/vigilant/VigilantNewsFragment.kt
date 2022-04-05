package com.gesecur.app.ui.vigilant

import android.content.DialogInterface
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentVigilantNewsBinding
import com.gesecur.app.domain.models.NewsRegistry
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.dialog.KeepAliveVigilantDialog
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.vigilant.worker.NewsReminderScheduleWorker
import com.gesecur.app.utils.getCurrentLocation
import com.gesecur.app.utils.showAlert
import com.gesecur.app.utils.toToolbarFormat
import com.gesecur.app.utils.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Duration
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

@ToolbarOptions(
    showToolbar = true)
class VigilantNewsFragment : BaseFragment(R.layout.fragment_vigilant_news) {

    private val binding by viewBinding(FragmentVigilantNewsBinding::bind)
    private val viewModel by sharedViewModel<VigilantViewModel>()

    companion object {
        const val WORK_NAME = "scheduledNewsWork"
    }

    override fun setupViews() {
        setTitle(title = LocalDate.now().toToolbarFormat())

        with(binding) {
            disableAll()

            btnInitTurn.setOnClickListener {
                startTurn()
            }

            btnEndTurn.setOnClickListener {
                endTurn()
            }

            registryContainer.setOnClickListener { viewModel.goToRegistries() }
            observationsContainer.setOnClickListener { viewModel.goToObservations() }

            btnNoUrgent.setOnClickListener {
                viewModel.addNewRegistry(viewModel.currentTurn.value!!.id, NewsRegistry.TYPE.REQUIREMENT_NON_URGENT)
            }
            btnNoNews.setOnClickListener {
                viewModel.addNewRegistry(viewModel.currentTurn.value!!.id, NewsRegistry.TYPE.NO_NEWS)
            }
            btnEmergency.setOnClickListener {
                viewModel.addNewRegistry(viewModel.currentTurn.value!!.id, NewsRegistry.TYPE.URGENT)
            }
        }
    }

    override fun stateManagedViewModels() = arrayListOf(viewModel)

    override fun setupViewModels() {
        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is VigilantViewModel.Action.TurnStarted -> onTurnStarted()
                is VigilantViewModel.Action.TurnFinished -> onTurnFinished()
                is VigilantViewModel.Action.TurnStartedSeconds -> printTurnStartedSeconds(it.seconds)
                is VigilantViewModel.Action.RegistrySucess -> toast(R.string.VIGILANT_NEWS_REGISTRY_SUCCESS)
            }
        })

        viewModel.currentTurn.observe(viewLifecycleOwner) {

        }

        viewModel.getCurrentTurn()
    }

    private fun disableAll() {
        with(binding) {
            btnEmergency.isEnabled = false
            btnNoNews.isEnabled = false
            btnNoUrgent.isEnabled = false
            observationsContainer.alpha = .3f
            registryContainer.alpha = .3f
            observationsContainer.isClickable = false
            registryContainer.isClickable = false
        }
    }

    private fun enableAll() {
        with(binding) {
            btnEmergency.isEnabled = true
            btnNoNews.isEnabled = true
            btnNoUrgent.isEnabled = true
            observationsContainer.alpha = 1f
            registryContainer.alpha = 1f
            observationsContainer.isClickable = true
            registryContainer.isClickable = true
        }
    }

    private fun startTurn() {
        showLoadingDialog()

        requireActivity().getCurrentLocation {
            it?.let { viewModel.startTurn(it.latitude, it.longitude) }

            manageKeepAliveNotification()
        }
    }

    private fun endTurn() {
        showLoadingDialog()

        requireActivity().getCurrentLocation {
            it?.let { viewModel.endTurn(viewModel.currentTurn.value!!.id, it.latitude, it.longitude) }
        }
    }

    private fun onTurnStarted() {
        binding.btnInitTurn.isVisible = false
        binding.activeTurnContainer.isVisible = true

        enableAll()
    }

    private fun onTurnFinished() {
        showAlert(text = R.string.VIGILANT_TURN_FINISHED_SUCCESS, listener = { _, _ ->
            disableAll()
            cancelPeriodicalNotification()
            viewModel.closeSession()

            navigateToSplash()
        })
    }

    private fun printTurnStartedSeconds(duration: Duration) {
        binding.tvActiveTurnTimer.text = String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            duration.toHours(),
            duration.toMinutes() % 60,
            duration.seconds % 60)
    }

    private fun manageKeepAliveNotification() {
        KeepAliveVigilantDialog(requireContext()) { generatePeriodicalNotification(it) }.show()
    }

    private fun generatePeriodicalNotification(time: Int) {
        val periodicWork =
            PeriodicWorkRequestBuilder<NewsReminderScheduleWorker>(time.toLong(), TimeUnit.MINUTES)
                .setInitialDelay(time.toLong(), TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWork
            )
    }

    private fun cancelPeriodicalNotification() {
        WorkManager.getInstance(requireContext())
            .cancelAllWork()
    }
}