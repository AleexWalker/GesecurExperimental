package com.gesecur.app.ui.common.dialog.material

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.MaterialItemTextBinding
import com.gesecur.app.domain.models.Product


class SearchProductAdapter : ListAdapter<Product, SearchProductAdapter.ProductHolder>(ProductComparator) {

    var onItemClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.material_item_text, parent, false)
        return ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(MaterialItemTextBinding::bind)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(layoutPosition))
            }
        }

        fun bind(item: Product) {
            with(binding) {
                tvName.text = item.description
            }
        }
    }

    object ProductComparator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}