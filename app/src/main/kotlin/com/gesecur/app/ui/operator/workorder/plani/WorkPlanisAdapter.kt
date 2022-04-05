package com.gesecur.app.ui.operator.workorder.plani

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
import com.gesecur.app.domain.models.WorkPlanification
import com.gesecur.app.utils.toHour


class WorkPlanisAdapter : ListAdapter<WorkPlanification, WorkPlanisAdapter.WorkPLanificationsHolder>(WorkOrderComparator) {

    var onItemClick: ((WorkPlanification) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkPLanificationsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.work_order_item, parent, false)
        return WorkPLanificationsHolder(view)
    }

    override fun onBindViewHolder(holder: WorkPLanificationsHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class WorkPLanificationsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(WorkOrderItemBinding::bind)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(layoutPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: WorkPlanification) {
            with(binding) {
                tvDesc.text = item.desc
                tvIdentifier.apply {
                    text = binding.root.context.getString(R.string.WORK_PLANI_IDENTIFIER, item.planiId?.toString() ?: "X")
                    setTextColor(Color.parseColor(item.stateColor))
                }

                tvHour.text = "${item.dateIni?.toHour()} - ${item.dateEnd?.toHour() ?: ""}"
                tvLocation.text = item.address
            }
        }
    }

    object WorkOrderComparator : DiffUtil.ItemCallback<WorkPlanification>() {
        override fun areItemsTheSame(oldItem: WorkPlanification, newItem: WorkPlanification): Boolean {
            return oldItem.planiId == newItem.planiId
        }

        override fun areContentsTheSame(oldItem: WorkPlanification, newItem: WorkPlanification): Boolean {
            return oldItem == newItem
        }
    }
}