package com.sasfmlzr.motherless.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sasfmlzr.motherless.R
import com.sasfmlzr.motherless.di.core.FragmentComponent
import com.sasfmlzr.motherless.di.core.Injector
import com.sasfmlzr.motherless.view.ErrorScreenView
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<VM : ViewModel, B : ViewDataBinding>
protected constructor(private val viewModelClass: KClass<VM>) : Fragment() {

    protected lateinit var initJob: Job

    abstract fun initData()

    protected abstract fun inject(component: FragmentComponent)

    protected abstract fun getLayoutId(): Int
    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    protected var errorScreenView: ErrorScreenView? = null
    protected var dataLayout: NestedScrollView? = null
    protected var isNetworkPage = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
        exception.printStackTrace()
        initJob.cancel()
        CoroutineScope(Dispatchers.Main).launch {
            errorScreenView?.setState(ErrorScreenView.State.ERROR)
            dataLayout?.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(Injector.fragmentComponent())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        viewModel = viewModelFactory.create(viewModelClass.java)

        errorScreenView = binding.root.findViewById(R.id.error_screen)

        if (isNetworkPage) {

            dataLayout = binding.root.findViewById(R.id.data_layout)
            if (errorScreenView != null) {
                dataLayout?.visibility = View.GONE
                errorScreenView?.setState(ErrorScreenView.State.RUNNING)
            }
            setRetryErrorListener()
        }

        initData()
        return view
    }

    override fun onStop() {
        if (::initJob.isInitialized && isNetworkPage) {
            initJob.cancel()
        }
        super.onStop()
    }

    protected open fun setRetryErrorListener() {
        println("Retry button has clicked")
    }
}
