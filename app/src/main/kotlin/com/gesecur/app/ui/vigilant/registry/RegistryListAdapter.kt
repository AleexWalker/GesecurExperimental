package com.gesecur.app.ui.vigilant.registry

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.RegistryNewsItemBinding
import com.gesecur.app.domain.models.NewsRegistry
import com.gesecur.app.utils.toHour


class RegistryListAdapter : ListAdapter<NewsRegistry, RegistryListAdapter.NewsRegistryHolder>(NewsRegistryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRegistryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.registry_news_item, parent, false)
        return NewsRegistryHolder(view)
    }

    override fun onBindViewHolder(holder: NewsRegistryHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class NewsRegistryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(RegistryNewsItemBinding::bind)

        fun bind(item: NewsRegistry) {
            with(binding) {
                tvDesc.text = item.statusDesc
                newsMarker.backgroundTintList = ColorStateList.valueOf(getBackgroundTintByRegistryType(
                                                    newsMarker.context,
                                                    item))
                tvTime.text = item.date?.toHour()
            }

        }

        private fun getBackgroundTintByRegistryType(context: Context, item: NewsRegistry): Int {
            with(context.resources) {
                return when(item.type) {
                    NewsRegistry.TYPE.NO_NEWS -> getColor(R.color.no_news_button_color)
                    NewsRegistry.TYPE.REQUIREMENT_NON_URGENT -> getColor(R.color.no_urgent_button_color)
                    NewsRegistry.TYPE.URGENT -> getColor(R.color.emergency_button_color)
                    else -> getColor(R.color.emergency_button_color)
                }
            }
        }
    }

    object NewsRegistryComparator : DiffUtil.ItemCallback<NewsRegistry>() {
        override fun areItemsTheSame(oldItem: NewsRegistry, newItem: NewsRegistry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsRegistry, newItem: NewsRegistry): Boolean {
            return oldItem == newItem
        }
    }
}