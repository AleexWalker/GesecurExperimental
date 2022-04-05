package com.gesecur.app.ui.operator

import android.graphics.Point
import androidx.annotation.IdRes
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorPlaniManagementBinding
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.utils.DateUtils
import com.gesecur.app.utils.toToolbarFormat
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.LocalDate

@ToolbarOptions(
    showToolbar = true)
class OperatorManagementPlaniFragment : BaseFragment(R.layout.fragment_operator_plani_management) {

    private val binding by viewBinding(FragmentOperatorPlaniManagementBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()

    val calendarAdapter = CalendarAdapter()

    override fun setupViews() {
        setTitle(title = LocalDate.now().toToolbarFormat())
        configureCalendarItems()

        with(binding) {
            typeToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if(isChecked)
                    onVisualizationTypeSelected(checkedId)
            }

            calendarAdapter.onItemClick = {
                viewModel.selectDateForPlani(it)
            }

            val display = requireActivity().windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)

            calendarAdapter.itemWidth = (size.x.toDouble() / 8.5).toInt()

            rvCalendar.adapter = calendarAdapter
            with(rvCalendar.layoutManager as LinearLayoutManager) {
                //reverseLayout = true
                stackFromEnd = true
            }
        }
    }

    override fun setupViewModels() {
        viewModel.viewAction.observe(this) {
            when (it) {

            }
        }
    }

    private fun configureCalendarItems() {
        val weekDays = DateUtils.generateWeekDates()

        calendarAdapter.submitList(weekDays.map { CalendarAdapter.CalendarItem(it, false) })

        with(viewModel.selectedDate.value ?: LocalDate.now()) {
            calendarAdapter.select(this)
            viewModel.selectDateForPlani(this)
        }
    }

    private fun onVisualizationTypeSelected(@IdRes buttonId: Int) {
        with(binding) {
            navWorkplanis.findNavController().navigate(
                    if(buttonId == btnList.id) R.id.operatorWorkPlaniListFragment
                    else R.id.operatorWorkPlaniMapFragment)
        }
    }
}