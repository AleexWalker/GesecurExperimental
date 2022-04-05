package com.gesecur.app.ui.operator.workorder.plani

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.PersonalItemBinding
import com.gesecur.app.databinding.PlanificationInternalItemBinding
import com.gesecur.app.domain.models.Personal
import com.gesecur.app.utils.formatRender


class PlanificationInternalAdapter : ListAdapter<PlanificationInternalAdapter.PlanificationInternalItem, PlanificationInternalAdapter.PlanificationInternalItemViewHolder>(PlanificationInternalItemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanificationInternalItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.planification_internal_item, parent, false)
        return PlanificationInternalItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanificationInternalItemViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class PlanificationInternalItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(PlanificationInternalItemBinding::bind)

        fun bind(item: PlanificationInternalItem) {
            with(binding) {
                tvItemDescription.text = item.desc
                tvPlanned.text = tvPlanned.context.getString(R.string.WORK_PLANIFICATION_ITEM_PLANNED, item.totalItems)
                tvRest.text = tvRest.context.getString(R.string.WORK_PLANIFICATION_ITEM_REST, item.restItems)
            }
        }
    }

    object PlanificationInternalItemComparator : DiffUtil.ItemCallback<PlanificationInternalItem>() {
        override fun areItemsTheSame(oldItem: PlanificationInternalItem, newItem: PlanificationInternalItem): Boolean {
            return oldItem.desc == newItem.desc
        }

        override fun areContentsTheSame(oldItem: PlanificationInternalItem, newItem: PlanificationInternalItem): Boolean {
            return oldItem == newItem
        }
    }


    data class PlanificationInternalItem(
        val desc: String,
        val totalItems: Int,
        val restItems: Int
    )
}