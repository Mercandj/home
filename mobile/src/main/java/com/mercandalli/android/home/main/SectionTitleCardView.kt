package com.mercandalli.android.home.main

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import com.mercandalli.android.home.R

class SectionTitleCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        setPadding(0, resources.getDimensionPixelOffset(R.dimen.default_space_quart), 0, 0)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_size_xl))
    }

}