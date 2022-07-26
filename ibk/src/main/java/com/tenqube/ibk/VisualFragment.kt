package com.tenqube.ibk

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tenqube.ibk.bridge.AndroidUIBridge
import com.tenqube.ibk.databinding.FragmentMainIbkBinding
import com.tenqube.ibk.di.IBKServiceLocator
import com.tenqube.shared.util.Utils
import com.tenqube.shared.webview.WebViewManager
import com.tenqube.shared.webview.WebViewParam
import com.tenqube.visualbase.domain.user.command.CreateUser
import com.tenqube.visualbase.infrastructure.framework.parser.rcs.RcsCatchReceiver
import com.tenqube.visualbase.infrastructure.framework.parser.sms.SMSCatchReceiver
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
        setupSwipeRefreshView()
        setupWebView()
        setupProgressEvents()
        parseArg()?.let {
            it.url?.let {  url ->
                viewModel.start("${BASE_URL}receipt?$url")
            } ?: viewModel.start(URL, it.user!!)
        } ?: requireActivity().finish()
        setupEvents()
    }

    private fun setupSwipeRefreshView() {
        with(viewDataBinding.swipeRefreshLayout) {
            this.isEnabled = false
            this.setColorSchemeResources(
                R.color.colorPopupRed,
                R.color.colorPopupRed,
                R.color.colorPopupRed
            )
            this.setOnRefreshListener {
                this.isRefreshing = false
                viewDataBinding.webView.reload()
            }
        }
    }

    private fun setupEvents() {
        viewModel.url.observe(this.viewLifecycleOwner) {
            viewDataBinding.webView.loadUrl(it)
        }

        viewModel.showAd.observe(this.viewLifecycleOwner) {
            viewDataBinding.container.addView(createCardView(it))
        }

        viewModel.hideAd.observe(this.viewLifecycleOwner) {
            viewDataBinding.container.allViews.firstOrNull { it is CardView }?.let {
                viewDataBinding.container.removeView(
                    it
                )
            }
        }
        viewModel.refreshEnabled.observe(this.viewLifecycleOwner) {
            viewDataBinding.swipeRefreshLayout.isEnabled = it
        }
    }

    private fun createCardView(view: View): CardView? {
        val adContainer = CardView(requireContext())
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(Utils.dpToPx(10), Utils.dpToPx(16), Utils.dpToPx(10), Utils.dpToPx(16))
        params.gravity = Gravity.BOTTOM
        adContainer.layoutParams = params
        adContainer.radius = Utils.dpToPx(13).toFloat()
        adContainer.setCardBackgroundColor(Color.parseColor("#00000000"))
        adContainer.addView(view)
        return adContainer
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

        viewModel.error.observe(this.viewLifecycleOwner) {
            viewDataBinding.webView.loadUrl(
                "javascript:window.onSyncError();"
            )
        }
    }

    companion object {
        const val BASE_URL = "https://d34db13xxji3zw.cloudfront.net/"
        const val URL = "${BASE_URL}?v=1.0&dv=1.0"
        const val PROGRESS_URL = "${BASE_URL}loading#type=bulk"
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
    val url: String? = null,
    val user: CreateUser? = null
) : Serializable
