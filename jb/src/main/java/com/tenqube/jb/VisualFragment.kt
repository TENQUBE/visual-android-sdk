package com.tenqube.jb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tenqube.jb.databinding.MainFragmentJbBinding
import com.tenqube.shared.webview.WebViewManager
import com.tenqube.shared.webview.WebViewParam

class VisualFragment : Fragment() {

    companion object {
        fun newInstance() = VisualFragment()
        const val URL = "https://d34db13xxji3zw.cloudfront.net/?v=1.0&dv=1.0"
    }

    private lateinit var viewModel: VisualViewModel
    private lateinit var viewDataBinding: MainFragmentJbBinding
    private lateinit var webViewManager: WebViewManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[VisualViewModel::class.java]
        viewDataBinding = MainFragmentJbBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }

        viewDataBinding.search.setOnClickListener {
            viewModel.start(viewDataBinding.url.text.toString())
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedDispatcher()
        setupLifecycleOwner()
        setupEvents()
        setupWebView()
        start()
    }

    private fun start() {
        getUrl().run {
            viewDataBinding.url.setText(this)
            viewModel.start(this)
        }
    }

    private fun getUrl(): String {
        return activity?.intent?.getStringExtra("url") ?: URL
    }

    private fun setupLifecycleOwner() {
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupEvents() {
        viewModel.url.observe(this.viewLifecycleOwner) {
            viewDataBinding.webView.loadUrl(it)
        }
    }

    private fun setupWebView() {
        with(viewDataBinding.webView) {
            webViewManager = WebViewManager(
                WebViewParam(this)
            )
            webViewManager.setupWebView()
        }
    }

    private fun setupOnBackPressedDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewDataBinding.webView.canGoBack()) {
                    viewDataBinding.webView.goBack()
                } else {
                    activity?.finish()
                }
            }
        })
    }
}
