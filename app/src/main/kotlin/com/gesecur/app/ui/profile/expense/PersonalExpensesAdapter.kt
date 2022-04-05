package com.gesecur.app.ui.profile.expense

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
import com.gesecur.app.databinding.ExpenseItemBinding
import com.gesecur.app.domain.models.Expense
import com.gesecur.app.utils.toGesecurFormat


class PersonalExpensesAdapter : ListAdapter<Expense, PersonalExpensesAdapter.ExpenseHolder>(
    ExpenseComparator
) {

    var onItemDeleteClick: ((Expense) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item, parent, false)
        return ExpenseHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class ExpenseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(ExpenseItemBinding::bind)

        init {
            binding.btnDelete.setOnClickListener {
                onItemDeleteClick?.invoke(getItem(layoutPosition))
            }
        }

        fun bind(item: Expense) {
            with(binding) {
                tvDesc.text = item.description
                tvType.text = item.typeDesc
                tvDate.text = item.date?.toGesecurFormat()
                tvStatus.text = item.statusDesc

                with(tvQuantity.context) {
                    tvQuantity.text = getString(R.string.PROFILE_EXPENSE_QUANTITY, item.quantity)
                    tvPrice.text = getString(R.string.PROFILE_EXPENSE_PRICE, item.price)

                    imgExpense.layout(0,0,0,0);

                    item.file?.let {
                        imgExpense.isVisible = true

                        Glide.with(this)
                            .load(it)
                            .fitCenter()
                            .into(imgExpense)
                    } ?: kotlin.run {
                        imgExpense.setImageDrawable(null)
                    }
                }
            }

        }
    }

    object ExpenseComparator : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}