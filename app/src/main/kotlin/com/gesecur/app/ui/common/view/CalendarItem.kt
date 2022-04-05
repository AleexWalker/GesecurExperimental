package com.gesecur.app.ui.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.CalendarItemBinding
import com.gesecur.app.utils.getDayNameAndNumber
import com.google.android.material.card.MaterialCardView
import java.time.LocalDate

class CalendarItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.GesecurCalendar
) : MaterialCardView(context, attrs, defStyle) {

    private val binding by viewBinding(CalendarItemBinding::bind)

    private lateinit var date: LocalDate

    var callback: SelectionCallback? = null

    init {
        inflate(context, R.layout.calendar_item, this)
        manageView()
    }

    private fun manageView() {
        binding.root.setOnClickListener {
            select()
        }
    }

    fun select() {
        isSelected = true

        callback?.onSelected()
    }

    fun unselect() {
        isSelected = false
    }

    fun setDate(date: LocalDate) {
        this.date = date

        with(date.getDayNameAndNumber()) {
            binding.tvDayName.text = first
            binding.tvDayNum.text = second
        }
    }

    interface SelectionCallback {
        fun onSelected()
    }
}