package com.gesecur.app.ui.common.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.gesecur.app.R
import com.gesecur.app.databinding.ViewToolbarBinding

class GesecurToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MaterialToolbar(context, attrs, defStyle) {

    private val binding by viewBinding(ViewToolbarBinding::bind)

    enum class Type {
        EMPTY,
        TITLE
    }

    enum class HomeButton {
        NONE,
        BACK
    }

    var type = Type.TITLE
        set(value) {
            field = value
            setToolbarType(type)
        }

    init {
        inflate(context, R.layout.view_toolbar, this)
    }

    override fun setTitle(resId: Int) {
        binding.toolbarTitle.setText(resId)
    }

    override fun setTitle(title: CharSequence?) {
        binding.toolbarTitle.text = title
    }

    private fun setToolbarType(type: Type) {
        with(binding) {
            when (type) {
                Type.EMPTY -> {
                    toolbarTitle.visibility = View.GONE
                }

                Type.TITLE -> {
                    toolbarTitle.visibility = View.VISIBLE
                }
            }
        }
    }

    fun setExtra(image: Int, callback: (() -> Unit)) {
        binding.btnExtra.isVisible = true
        binding.btnExtra.setImageResource(image)
        binding.btnExtra.setOnClickListener { callback() }
    }

    fun hideExtra() {
        binding.btnExtra.isVisible = false
    }
}