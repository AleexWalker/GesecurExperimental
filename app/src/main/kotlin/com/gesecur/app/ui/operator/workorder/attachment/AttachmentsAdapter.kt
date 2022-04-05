package com.gesecur.app.ui.operator.workorder.attachment

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
import com.gesecur.app.databinding.AttachmentItemBinding
import com.gesecur.app.domain.models.Attachment
import com.gesecur.app.utils.isImageExtension
import com.gesecur.app.utils.toGesecurFormat


class AttachmentsAdapter : ListAdapter<Attachment, AttachmentsAdapter.AttachmentHolder>(AttachmentComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attachment_item, parent, false)
        return AttachmentHolder(view)
    }

    override fun onBindViewHolder(holder: AttachmentHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    var pdfClickCallback: ((String) -> Unit)? = null

    inner class AttachmentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding(AttachmentItemBinding::bind)

        fun bind(item: Attachment) {
            with(binding) {
                tvAttachDesc.text = item.desc
                tvNotes.text = item.notes
                tvDate.text = item.date?.toGesecurFormat()

                imgAttached.layout(0,0,0,0);

                item.image?.let {
                    if(!it.isImageExtension()) {
                        imgAttached.isVisible = false
                        tvAttachmentLink.isVisible = true

                        tvAttachmentLink.setOnClickListener { _ ->
                            pdfClickCallback?.invoke(it)
                        }
                    }
                    else {
                        imgAttached.isVisible = true
                        tvAttachmentLink.isVisible = false

                        Glide.with(imgAttached.context)
                            .load(it)
                            .fitCenter()
                            .into(imgAttached)
                    }
                } ?: kotlin.run {
                    imgAttached.isVisible = false
                    imgAttached.setImageDrawable(null)
                    tvAttachmentLink.isVisible = false
                }
            }

        }
    }



    object AttachmentComparator : DiffUtil.ItemCallback<Attachment>() {
        override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {
            return oldItem == newItem
        }
    }
}