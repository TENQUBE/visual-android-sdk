package com.tenqube.jb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tenqube.jb.bridge.ui.AndroidUIBridge
import com.tenqube.jb.databinding.MainFragmentJbBinding
import com.tenqube.shared.webview.WebViewManager
import com.tenqube.shared.webview.WebViewParam
import com.tenqube.webui.UIServiceBuilder

class VisualFragment : Fragment() {

    companion object {
        fun newInstance() = VisualFragment()
        const val URL = "file:///android_asset/sample.html"
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

        viewDataBinding.test.setOnClickListener {
            goTest()
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedDispatcher()
        setupLifecycleOwner()
        setupEvents()
        setupWebView()
        setupSwipeRefreshView()
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

    private fun goTest() {
        viewDataBinding.webView.loadUrl("file:///android_asset/sample.html")
    }

    private fun setupWebView() {
        with(viewDataBinding.webView) {
            webViewManager = WebViewManager(
                WebViewParam(this)
            )
            webViewManager.setupWebView()

            val uiService = UIServiceBuilder()
                .activity(activity as AppCompatActivity)
                .refreshCallback {
                    setRefreshEnabled(it)
                }
                .build()

            val ui = AndroidUIBridge(this, uiService)
            this.addJavascriptInterface(ui, ui.bridgeName)
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

    private fun setupSwipeRefreshView() {
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener {
            viewDataBinding.swipeRefreshLayout.isRefreshing = false
            viewDataBinding.webView.reload()
        }
    }

    private fun setRefreshEnabled(enabled: Boolean) {
        viewDataBinding.swipeRefreshLayout.isEnabled = enabled
    }
}
