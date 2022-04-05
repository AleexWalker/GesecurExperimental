package com.gesecur.app.ui.operator.workorder.job

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.JobItemBinding
import com.gesecur.app.domain.models.Job
import com.gesecur.app.domain.models.Material


class JobsAdapter : ListAdapter<Job, JobsAdapter.JobHolder>(JobComparator) {

    var onItemDeleteClick: ((Job) -> Unit)? = null
    var onItemPlusClick: ((Job, Int) -> Unit)? = null
    var onItemMinusClick: ((Job, Int) -> Unit)? = null

    var editable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
        return JobHolder(view)
    }

    override fun onBindViewHolder(holder: JobHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class JobHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(JobItemBinding::bind)

        init {
            with(binding) {
                btnDelete.setOnClickListener {
                    onItemDeleteClick?.invoke(getItem(layoutPosition))
                }

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

                btnPlus.isVisible = editable
                btnDelete.isVisible = editable
                btnMin.isVisible = editable
            }
        }

        fun bind(item: Job) {
            with(binding) {
                tvDesc.text = item.description.plus("  (${tvDesc.context.getString(R.string.JOB_DURATION_TEXT, item.duration)})")
                tvQuantity.text = item.quantity.toString()
                btnDelete.visibility = if(item.extra) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    object JobComparator : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem == newItem
        }
    }
}