package com.gesecur.app.ui.common.itemdecorations

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class VerticalMarginItemDecoration(@DimenRes private val spaceHeightDimen: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spaceHeight = parent.resources.getDimensionPixelSize(spaceHeightDimen) / 2

        with(outRect) {
            if (parent.getChildAdapterPosition(view) != 0) {
                top = spaceHeight
            }

            if (parent.getChildAdapterPosition(view) != state.itemCount - 1) {
                bottom = spaceHeight
            }
        }
    }
}