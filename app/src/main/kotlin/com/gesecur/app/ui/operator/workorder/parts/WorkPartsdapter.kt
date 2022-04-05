package com.gesecur.app.ui.operator.workorder.parts

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.WorkOrderItemBinding
import com.gesecur.app.domain.models.WorkPart
import com.gesecur.app.utils.toHour


class WorkPartsdapter : ListAdapter<WorkPart, WorkPartsdapter.WorkOrderHolder>(WorkOrderComparator) {

    var onItemClick: ((WorkPart) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOrderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.work_order_item, parent, false)
        return WorkOrderHolder(view)
    }

    override fun onBindViewHolder(holder: WorkOrderHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class WorkOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(WorkOrderItemBinding::bind)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(layoutPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: WorkPart) {
            with(binding) {
                tvDesc.text = item.desc
                tvIdentifier.apply {
                    text = binding.root.context.getString(R.string.WORK_PART_IDENTIFIER, item.id?.toString() ?: "X")
                    setTextColor(Color.parseColor(item.stateColor))
                }

                tvHour.text = "${item.dateIni?.toHour()} - ${item.dateEnd?.toHour() ?: ""}"
                tvLocation.text = item.address
            }
        }
    }

    object WorkOrderComparator : DiffUtil.ItemCallback<WorkPart>() {
        override fun areItemsTheSame(oldItem: WorkPart, newItem: WorkPart): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WorkPart, newItem: WorkPart): Boolean {
            return oldItem == newItem
        }
    }
}