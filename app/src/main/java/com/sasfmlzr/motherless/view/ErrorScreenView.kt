package com.sasfmlzr.motherless.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.sasfmlzr.motherless.databinding.ViewErrorBinding

class ErrorScreenView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binder: ViewErrorBinding by lazy {
        ViewErrorBinding.inflate(LayoutInflater.from(context))
    }

    init {
        val params = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(binder.root, params)
        setState(State.OK)
    }

    fun setState(errorState: State) {
        when (errorState) {
            State.OK -> {
                binder.root.visibility = View.GONE
            }
            State.ERROR -> {
                binder.root.visibility = View.VISIBLE
                binder.progressBar.visibility = View.GONE
                binder.message.visibility = View.VISIBLE
                binder.retryButton.visibility = View.VISIBLE
            }
            State.RUNNING -> {
                binder.root.visibility = View.VISIBLE
                binder.progressBar.visibility = View.VISIBLE
                binder.message.visibility = View.GONE
                binder.retryButton.visibility = View.GONE
            }
        }
    }

    fun setRetryOnClickListener(l: (View) -> Unit) {
        binder.retryButton.setOnClickListener(l)
    }

    enum class State { OK, RUNNING, ERROR }
}
