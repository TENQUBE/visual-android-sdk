package com.tenqube.ibk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.lifecycle.ViewModelProvider
import com.tenqube.ibk.bridge.AndroidUIBridge
import com.tenqube.ibk.databinding.FragmentMainIbkBinding
import com.tenqube.shared.webview.WebViewManager
import com.tenqube.shared.webview.WebViewParam

class VisualFragment : Fragment() {

    private lateinit var viewModel: VisualViewModel
    private lateinit var viewDataBinding: FragmentMainIbkBinding
    private lateinit var webViewManager: WebViewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[VisualViewModel::class.java]
        viewDataBinding = FragmentMainIbkBinding.inflate(inflater, container, false)
            .apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycleOwner()
        setupWebView()
    }

    private fun setupLifecycleOwner() {
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupWebView() {
        with(viewDataBinding.webView) {
            webViewManager = WebViewManager(
                WebViewParam(this)
            )
            webViewManager.setupWebView()
            setupBridges(this)
        }
    }

    private fun setupBridges(webView: WebView) {
        AndroidUIBridge(webView, viewModel).let { ui ->
            webView.addJavascriptInterface(ui, ui.bridgeName)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = VisualFragment()
    }
}