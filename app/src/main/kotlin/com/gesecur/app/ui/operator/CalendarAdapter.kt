package com.gesecur.app.ui.operator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.CalendarItemBinding
import com.gesecur.app.utils.getDayNameAndNumber
import java.time.LocalDate


class CalendarAdapter : ListAdapter<CalendarAdapter.CalendarItem, CalendarAdapter.WorkOrderHolder>(WorkOrderComparator) {

    var selectedItem: CalendarItem? = null
        set(value) {
            value?.selected = true
            field = value

            notifyItemChanged(currentList.indexOf(value))
        }


    var onItemClick: ((LocalDate) -> Unit)? = null

    var itemWidth = 0

    fun select(date: LocalDate) {
        selectedItem = currentList[currentList.indexOf(CalendarItem(date, false))]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOrderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)

        view.layoutParams.width = itemWidth

        return WorkOrderHolder(view)
    }

    override fun onBindViewHolder(holder: WorkOrderHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class WorkOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(CalendarItemBinding::bind)

        init {
            itemView.setOnClickListener {
                selectedItem?.selected = false
                with(getItem(layoutPosition)) {
                    val index = currentList.indexOf(selectedItem)

                    selectedItem = this
                    onItemClick?.invoke(date)

                    if(index > -1)
                        notifyItemChanged(index)
                }
            }
        }

        fun bind(item: CalendarItem) {
            with(binding) {
                with(item.date.getDayNameAndNumber()) {
                    tvDayName.text = first
                    tvDayNum.text = second
                }

                binding.root.isSelected = item.selected
            }
        }
    }

    object WorkOrderComparator : DiffUtil.ItemCallback<CalendarItem>() {
        override fun areItemsTheSame(oldItem: CalendarItem, newItem: CalendarItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CalendarItem, newItem: CalendarItem): Boolean {
            return oldItem.date == newItem.date && oldItem.selected == newItem.selected
        }
    }

    data class CalendarItem(
        val date: LocalDate,
        var selected: Boolean = false
    )
    {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is CalendarItem) return false

            if (date != other.date) return false

            return true
        }

        override fun hashCode(): Int {
            return date.hashCode()
        }
    }
}