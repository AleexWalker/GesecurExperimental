package com.gesecur.app.ui.incidences

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.gesecur.app.R
import com.gesecur.app.databinding.IncidenceItemBinding
import com.gesecur.app.domain.models.Incidence
import com.gesecur.app.utils.toGesecurFormat


class IncidencesAdapter : ListAdapter<Incidence, IncidencesAdapter.IncidenceHolder>(IncidencesComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidenceHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.incidence_item, parent, false)
        return IncidenceHolder(view)
    }

    override fun onBindViewHolder(holder: IncidenceHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class IncidenceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(IncidenceItemBinding::bind)

        fun bind(item: Incidence) {
            with(binding) {
                tvDesc.text = item.description
                tvLocation.text = item.address
                tvDate.text = item.date?.toGesecurFormat()

                imgIncidence.layout(0,0,0,0);

                item.file.let {
                    imgIncidence.isVisible = true

                    Glide.with(imgIncidence.context)
                        .load(it)
                        .fitCenter()
                        .into(imgIncidence)
                } ?: kotlin.run {
                    imgIncidence.setImageDrawable(null)
                }
            }

        }
    }

    object IncidencesComparator : DiffUtil.ItemCallback<Incidence>() {
        override fun areItemsTheSame(oldItem: Incidence, newItem: Incidence): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Incidence, newItem: Incidence): Boolean {
            return oldItem == newItem
        }
    }
}