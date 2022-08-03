package com.tenqube.ibkreceipt

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tenqube.ibkreceipt.bridge.AndroidUIBridge
import com.tenqube.ibkreceipt.databinding.FragmentMainIbkBinding
import com.tenqube.ibkreceipt.di.IBKServiceLocator
import com.tenqube.shared.util.Utils
import com.tenqube.shared.webview.WebViewManager
import com.tenqube.shared.webview.WebViewParam
import com.tenqube.visualbase.domain.user.command.CreateUser
import java.io.Serializable

class VisualFragment : Fragment() {

    private lateinit var viewModel: VisualViewModel
    private lateinit var viewDataBinding: FragmentMainIbkBinding
    private lateinit var webViewManager: WebViewManager
    private var adContainer: CardView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            IBKServiceLocator.provideVisualViewModel(requireActivity() as AppCompatActivity)
        )[VisualViewModel::class.java]
        viewDataBinding = FragmentMainIbkBinding.inflate(inflater, container, false)
            .apply {
                viewmodel = viewModel
            }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycleOwner()
        setupOnBackPressedDispatcher()
        setupSwipeRefreshView()
        setupWebView()
        setupProgressEvents()
        start()
        setupEvents()
        setupErrorView()
    }

    private fun start() {
        if (shouldRequestOverlayPermission()) {
            startOverlay()
        } else {
            parseArg()?.let {
                it.url?.let { url ->
                    startReceipt(url)
                } ?: viewModel.start(getUrl(), it.user)
            } ?: viewModel.start(getUrl())
        }
    }

    private fun getUrl(): String {
        return String.format(URL, viewModel.getUrl())
    }

    private fun startOverlay() {
        viewDataBinding.webView.setBackgroundColor(Color.TRANSPARENT)
        viewDataBinding.container.setBackgroundColor(Color.TRANSPARENT)
        viewModel.start("${viewModel.getUrl()}permission")
    }

    private fun startReceipt(url: String) {
        viewDataBinding.webView.setBackgroundColor(Color.TRANSPARENT)
        viewDataBinding.container.setBackgroundColor(Color.TRANSPARENT)
        viewModel.start("${viewModel.getUrl()}receipt?$url")
    }

    private fun setupSwipeRefreshView() {
        with(viewDataBinding.swipeRefreshLayout) {
            this.isEnabled = false
            this.setOnRefreshListener {
                this.isRefreshing = false
                viewDataBinding.webView.reload()
            }
        }
    }

    private fun setupEvents() {
        viewModel.url.observe(this.viewLifecycleOwner) {
            Log.i("WEBVIEW", it)
            viewDataBinding.webView.loadUrl(it)
        }
        viewModel.showAd.observe(this.viewLifecycleOwner) {
            viewDataBinding.container.addView(createCardView(it))
        }
        viewModel.hideAd.observe(this.viewLifecycleOwner) {
            adContainer?.let {
                viewDataBinding.container.removeView(it)
                adContainer = null
            }
        }
        viewModel.refreshEnabled.observe(this.viewLifecycleOwner) {
            viewDataBinding.swipeRefreshLayout.isEnabled = it
        }
        viewModel.error.observe(this.viewLifecycleOwner) {
            viewModel.showToast(it)
            viewDataBinding.errorContainer.errorContainer.visibility = View.VISIBLE
        }
    }

    private fun setupErrorView() {
        viewDataBinding.errorContainer.retry.setOnClickListener {
            viewDataBinding.errorContainer.errorContainer.visibility = View.GONE
            start()
        }
    }

    private fun createCardView(view: View): CardView? {
        adContainer = CardView(requireContext())
        with(CardView(requireContext())) {
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(Utils.dpToPx(16), Utils.dpToPx(16), Utils.dpToPx(16), Utils.dpToPx(16))
            params.gravity = Gravity.BOTTOM
            this.layoutParams = params
            this.radius = Utils.dpToPx(13).toFloat()
            this.setCardBackgroundColor(Color.parseColor("#00000000"))
            this.addView(view)
            adContainer = this
        }

        return adContainer
    }

    private fun parseArg(): VisualIBKArg? {
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
    }

    private fun setupProgressEvents() {
        viewModel.isProgress.observe(this.viewLifecycleOwner) {
            if (it) {
                viewDataBinding.webView.loadUrl(viewModel.getUrl() + PROGRESS_URL)
            } else {
                viewDataBinding.webView.clearHistory()
                viewDataBinding.webView.loadUrl(getUrl())
            }
        }
        viewModel.progressCount.observe(this.viewLifecycleOwner) {
            viewDataBinding.webView.loadUrl(
                "javascript:window.onProgress(${it.now}, ${it.total});"
            )
        }
        viewModel.error.observe(this.viewLifecycleOwner) {
            viewDataBinding.webView.loadUrl(
                "javascript:window.onSyncError();"
            )
        }
    }

    private fun setupOnBackPressedDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewDataBinding.webView.canGoBack()) {
                    viewDataBinding.webView.goBack()
                } else {
                    activity?.overridePendingTransition(0, 0)
                    activity?.finish()
                }
            }
        })
    }

    private fun shouldRequestOverlayPermission(): Boolean {
        return Build.VERSION.SDK_INT >= 29 && !Settings.canDrawOverlays(requireContext().applicationContext)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_OVERLAY_PERMISSION -> {
                start()
            }
        }
    }

    companion object {
        const val URL = "%s?v=1.0&dv=1.0"
        const val PROGRESS_URL = "loading#type=bulk"
        const val VISUAL_IBK_ARG = "visual_ibk_arg"
        const val REQ_CODE_OVERLAY_PERMISSION = 10
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
    val url: String? = null,
    val user: CreateUser? = null
) : Serializable
