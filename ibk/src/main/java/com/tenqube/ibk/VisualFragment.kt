package com.tenqube.ibk

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tenqube.ibk.bridge.AndroidUIBridge
import com.tenqube.ibk.bridge.SystemBridge
import com.tenqube.ibk.bridge.VisualRepositoryBridge
import com.tenqube.ibk.databinding.FragmentMainIbkBinding
import com.tenqube.ibk.di.IBKServiceLocator
import com.tenqube.shared.webview.WebViewManager
import com.tenqube.shared.webview.WebViewParam
import com.tenqube.visualbase.domain.user.command.CreateUser
import java.io.Serializable

class VisualFragment : Fragment() {

    private lateinit var viewModel: VisualViewModel
    private lateinit var viewDataBinding: FragmentMainIbkBinding
    private lateinit var webViewManager: WebViewManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this,
            IBKServiceLocator.provideVisualViewModel(requireActivity() as AppCompatActivity))[VisualViewModel::class.java]
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
        setupProgressEvents()
        parseArg()?.let {
            viewModel.start(URL, it.user)
        } ?: requireActivity().finish()
    }

    private fun parseArg() : VisualIBKArg? {
        return arguments?.let {
            it.getSerializable(VISUAL_IBK_ARG) as VisualIBKArg
        }
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

    @SuppressLint("JavascriptInterface")
    private fun setupBridges(webView: WebView) {
        with(AndroidUIBridge(this, webView, viewModel)) {
            webView.addJavascriptInterface(this, this.bridgeName)
        }
        with(SystemBridge(this, webView, viewModel)) {
            webView.addJavascriptInterface(this, this.bridgeName)
        }
        with(VisualRepositoryBridge(this, webView, viewModel)) {
            webView.addJavascriptInterface(this, this.bridgeName)
        }
    }

    private fun setupProgressEvents() {
        viewModel.isProgress.observe(this.viewLifecycleOwner) {
            if (it) {
                viewDataBinding.webView.loadUrl(PROGRESS_URL)
            } else {
                viewDataBinding.webView.loadUrl(URL)
            }
        }

        viewModel.progressCount.observe(this.viewLifecycleOwner) {
            viewDataBinding.webView.loadUrl(
                "javascript:window.onProgress(${it.now}, ${it.total});"
            )
        }
    }

    companion object {
        const val URL = "https://d34db13xxji3zw.cloudfront.net/?v=1.0&dv=1.0"
        const val PROGRESS_URL = "${URL}loading/#type=bulk"
        const val VISUAL_IBK_ARG = "visual_ibk_arg"
        @JvmStatic
        fun newInstance(arg: VisualIBKArg): VisualFragment {
            return VisualFragment().apply {
                this.arguments = Bundle().apply {
                    putSerializable(VISUAL_IBK_ARG, arg)
                }
            }
        }
    }
}

data class VisualIBKArg(
    val user: CreateUser
) : Serializable
