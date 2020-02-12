package com.sasfmlzr.motherless.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.sasfmlzr.motherless.adapter.VideoAdapter
import com.sasfmlzr.motherless.databinding.ViewVideosBinding
import com.sasfmlzr.motherless.entity.UnknownPreviewData

class CustomViewVideo @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binder: ViewVideosBinding by lazy {
        ViewVideosBinding.inflate(LayoutInflater.from(context))
    }

    init {
        val params = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(binder.root, params)

        val systemAttrs = intArrayOf(android.R.attr.text)
        val systemTypedArray = context.obtainStyledAttributes(attrs, systemAttrs)
        binder.text.text = systemTypedArray.getString(0)
        systemTypedArray.recycle()
    }

    fun setItems(list: List<UnknownPreviewData>? = listOf()) {
        binder.videoList.adapter = VideoAdapter(list!!, context)
    }
}
