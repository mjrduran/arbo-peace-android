package com.arbo.oracoes.presentation.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(
    val context: Context,
    @DimenRes itemOffsetId: Int
) : RecyclerView.ItemDecoration() {

    private val itemOffset: Int by lazy { context.resources.getDimensionPixelSize(itemOffsetId) }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(itemOffset, itemOffset, itemOffset, itemOffset)
    }
}