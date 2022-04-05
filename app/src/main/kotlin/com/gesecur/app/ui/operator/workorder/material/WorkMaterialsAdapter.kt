package com.gesecur.app.ui.operator.workorder.material

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.MaterialItemBinding
import com.gesecur.app.domain.models.Material
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class WorkMaterialsAdapter : ListAdapter<Material, WorkMaterialsAdapter.MaterialHolder>(MaterialComparator) {

    var onItemPlusClick: ((Material, Int) -> Unit)? = null
    var onItemMinusClick: ((Material, Int) -> Unit)? = null

    var editable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.material_item, parent, false)
        return MaterialHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class MaterialHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(MaterialItemBinding::bind)

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

                    with(getItem(layoutPosition)) {
                        if(value < this.planiQuantity ?: 0) {

                            value += 1;
                            onItemPlusClick?.invoke(this, value)
                            tvQuantity.text = value.toString()
                        }
                    }
                }

                btnPlus.visibility = if(editable) View.VISIBLE else View.INVISIBLE
                btnMin.visibility = if(editable) View.VISIBLE else View.INVISIBLE
            }
        }

        fun bind(item: Material) {
            with(binding) {
                tvName.text = Html.fromHtml(item.description ?: item.concept)
                tvQuantity.text = item.quantity.toString()
            }
        }
    }

    object MaterialComparator : DiffUtil.ItemCallback<Material>() {
        @ExperimentalSerializationApi
        override fun areItemsTheSame(oldItem: Material, newItem: Material): Boolean {
            return oldItem.id == newItem.id
        }

        @ExperimentalSerializationApi
        override fun areContentsTheSame(oldItem: Material, newItem: Material): Boolean {
            return oldItem == newItem
        }
    }
}