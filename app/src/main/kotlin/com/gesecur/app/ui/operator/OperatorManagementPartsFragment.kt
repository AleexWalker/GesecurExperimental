package com.gesecur.app.ui.operator

import android.graphics.Point
import androidx.annotation.IdRes
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorPartsManagementBinding
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.utils.DateUtils
import com.gesecur.app.utils.toToolbarFormat
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.LocalDate

@ToolbarOptions(
    showToolbar = true)
class OperatorManagementPartsFragment : BaseFragment(R.layout.fragment_operator_parts_management) {

    private val binding by viewBinding(FragmentOperatorPartsManagementBinding::bind)
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
                viewModel.selectDateForPart(it)
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
            viewModel.selectDateForPart(this)
        }
    }

//    private fun configureCalendarItem(calendarItem: CalendarItem, localDate: LocalDate, selected: Boolean = false) {
//        calendarItem.setDate(localDate)
//
//        calendarItem.callback = object : CalendarItem.SelectionCallback {
//            override fun onSelected() {
//
//                if(this@OperatorManagementFragment::calendarItemSelected.isInitialized)
//                    calendarItemSelected.unselect()
//
//                calendarItemSelected = calendarItem
//                calendarItem.isSelected = true
//
//                viewModel.selectDate(localDate)
//            }
//        }
//
//        if(selected) calendarItem.select()
//    }

    private fun onVisualizationTypeSelected(@IdRes buttonId: Int) {
        with(binding) {
            navWorkorders.findNavController().navigate(
                    if(buttonId == btnList.id) R.id.operatorWorkOrderListFragment
                    else R.id.operatorWorkOrderMapFragment)
        }
    }
}