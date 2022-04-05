package com.gesecur.app.ui.operator.workorder.parts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.OtherItemBinding
import com.gesecur.app.databinding.PersonalItemBinding
import com.gesecur.app.domain.models.Other
import com.gesecur.app.domain.models.Personal
import com.gesecur.app.utils.formatRender
import com.gesecur.app.utils.toHour
import com.gesecur.app.utils.toServerFormat


class PersonalAdapter : ListAdapter<Personal, PersonalAdapter.PersonalViewHolder>(PersonalComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.personal_item, parent, false)
        return PersonalViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonalViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class PersonalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(PersonalItemBinding::bind)

        fun bind(item: Personal) {
            with(binding) {
                tvPersonalName.text = item.getPersonalName()
                tvStart.text = tvStart.context.getString(R.string.WORK_PERSONAL_ITEM_INIT_DATE, item.initDate?.toLocalDate()?.formatRender() ?: "")
                tvEnd.text = tvEnd.context.getString(R.string.WORK_PERSONAL_ITEM_END_DATE, item.endDate?.toLocalDate()?.formatRender() ?: "")
            }
        }
    }

    object PersonalComparator : DiffUtil.ItemCallback<Personal>() {
        override fun areItemsTheSame(oldItem: Personal, newItem: Personal): Boolean {
            return oldItem.personalId == newItem.personalId
        }

        override fun areContentsTheSame(oldItem: Personal, newItem: Personal): Boolean {
            return oldItem == newItem
        }
    }
}