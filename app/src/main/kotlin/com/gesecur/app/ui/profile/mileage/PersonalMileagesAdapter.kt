package com.gesecur.app.ui.profile.mileage

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
import com.gesecur.app.databinding.MileageItemBinding
import com.gesecur.app.domain.models.Mileage
import com.gesecur.app.utils.toGesecurFormat


class PersonalMileagesAdapter : ListAdapter<Mileage, PersonalMileagesAdapter.MileageHolder>(
    MileageComparator
) {

    var onItemDeleteClick: ((Mileage) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MileageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mileage_item, parent, false)
        return MileageHolder(view)
    }

    override fun onBindViewHolder(holder: MileageHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class MileageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(MileageItemBinding::bind)

        init {
            binding.btnDelete.setOnClickListener {
                onItemDeleteClick?.invoke(getItem(layoutPosition))
            }
        }

        fun bind(item: Mileage) {
            with(binding) {
                tvDesc.text = item.description
                tvDate.text = item.date?.toGesecurFormat()
                tvStatus.text = item.statusDesc

                with(tvMileageIni.context) {
                    tvMileageIni.text = getString(R.string.PROFILE_MILEAGE_INITIAL, item.kmIni)
                    tvMileageEnd.text = getString(R.string.PROFILE_MILEAGE_END, item.kmFin)

                    imgMileage.layout(0,0,0,0);

                    item.file?.let {
                        imgMileage.isVisible = true

                        Glide.with(this)
                            .load(it)
                            .fitCenter()
                            .into(imgMileage)
                    } ?: kotlin.run {
                        imgMileage.setImageDrawable(null)
                    }
                }
            }

        }
    }

    object MileageComparator : DiffUtil.ItemCallback<Mileage>() {
        override fun areItemsTheSame(oldItem: Mileage, newItem: Mileage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Mileage, newItem: Mileage): Boolean {
            return oldItem == newItem
        }
    }
}