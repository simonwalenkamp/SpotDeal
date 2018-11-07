package com.walenkamp.spotdeal

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import android.graphics.drawable.Drawable



class Separator(context: Context) : RecyclerView.ItemDecoration() {
    private var mDivider: Drawable = context.getDrawable(R.drawable.rec_separator)!!

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        var left: Int = parent.paddingLeft
        var right: Int = parent.width - parent.paddingRight

        var childCount: Int = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
        super.onDrawOver(c, parent, state)
    }
}