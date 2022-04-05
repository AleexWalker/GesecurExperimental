package com.gesecur.app.ui.common.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.gesecur.app.R
import com.gesecur.app.databinding.ViewModalToolbarBinding
import com.gesecur.app.databinding.ViewToolbarBinding

class GesecurModalToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MaterialToolbar(context, attrs, defStyle) {

    private val binding by viewBinding(ViewModalToolbarBinding::bind)

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
        inflate(context, R.layout.view_modal_toolbar, this)
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

    fun setCloseAction(unit : () -> Unit)  {
        with(binding.btnClose) {
            setOnClickListener { unit.invoke() }
        }
    }

    fun setOptionButton(@StringRes optionText: Int, unit : () -> Unit)  {
        with(binding.btnOption) {
            setText(optionText)
            setOnClickListener { unit.invoke() }
        }
    }
}