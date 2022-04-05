package com.gesecur.app.ui.operator.workorder.other

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
import com.gesecur.app.domain.models.Other


class OtherAdapter : ListAdapter<Other, OtherAdapter.OtherHolder>(OtherComparator) {

    var onItemPlusClick: ((Other, Int) -> Unit)? = null
    var onItemMinusClick: ((Other, Int) -> Unit)? = null

    var editable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.other_item, parent, false)
        return OtherHolder(view)
    }

    override fun onBindViewHolder(holder: OtherHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class OtherHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(OtherItemBinding::bind)

        init {
            with(binding) {

                btnMin.setOnClickListener {
                    var value = tvQuantity.text.toString().toInt()

                    if (value > 0) {
                        value -= 1;
                        onItemMinusClick?.invoke(getItem(layoutPosition), value)
                        tvQuantity.text = value.toString()
                    }
                }

                btnPlus.setOnClickListener {
                    var value = tvQuantity.text.toString().toInt()

                    value += 1;
                    onItemPlusClick?.invoke(getItem(layoutPosition), value)
                    tvQuantity.text = value.toString()
                }

                btnPlus.visibility = if(editable) View.VISIBLE else View.INVISIBLE
                btnMin.visibility = if(editable) View.VISIBLE else View.INVISIBLE
            }
        }

        fun bind(item: Other) {
            with(binding) {
                tvName.text = item.description
                tvQuantity.text = item.quantity.toString()
            }
        }
    }

    object OtherComparator : DiffUtil.ItemCallback<Other>() {
        override fun areItemsTheSame(oldItem: Other, newItem: Other): Boolean {
            return oldItem.otherPartId == newItem.otherPartId
        }

        override fun areContentsTheSame(oldItem: Other, newItem: Other): Boolean {
            return oldItem == newItem
        }
    }
}