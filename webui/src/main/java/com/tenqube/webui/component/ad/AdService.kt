package com.tenqube.webui.component.ad

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.tenqube.webui.R
import com.tenqube.webui.dto.ShowAd

class AdService(private val context: Context) {
    companion object {
//        "ca-app-pub-1003660361092577/9395305141"
        const val AD_ID = "ca-app-pub-1003660361092577~7504035716"
        const val TEST_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    }

    init {
        MobileAds.initialize(context, "ca-app-pub-1003660361092577~2282746668")
    }

    fun showAd(request: ShowAd) {
        val builder = AdLoader.Builder(context, request.unitId)
        builder.forUnifiedNativeAd { unifiedNativeAd ->
            val adView: UnifiedNativeAdView = LayoutInflater.from(context)
                .inflate(R.layout.visual_admob_view, null) as UnifiedNativeAdView
            populateUnifiedNativeAdView(request, unifiedNativeAd, adView)
            request.callback(adView)
        }
        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()

        val adOptions: NativeAdOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()

        builder.withNativeAdOptions(adOptions)
        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
            }
        }).build()
        val nativeAdRequest = AdRequest.Builder()
            .build()
        adLoader.loadAd(nativeAdRequest)
    }

    fun hideAd() {
    }

    private fun populateUnifiedNativeAdView(
        adRequest: ShowAd,
        nativeAd: UnifiedNativeAd,
        adView: UnifiedNativeAdView
    ): View? {
        var bgColor = "#f0fbff"
        var titleColor = "#000000"
        var linkToColor = "#ffffff"
        var linkToBgColor = "#3684ff"

        if (adRequest.container != null) {
            bgColor = adRequest.container.bgColor
            adRequest.container.textColor?.let {
                titleColor = it
            }
        }
        setBgColor(adView, bgColor)
        if (
            adRequest.button != null
        ) {
            linkToBgColor = adRequest.button.bgColor
            adRequest.button.textColor?.let {
                linkToColor = it
            }
        }
        adView.headlineView = adView.findViewById(R.id.title)
        adView.iconView = adView.findViewById(R.id.icon_image)
        adView.bodyView = adView.findViewById(R.id.msg)
        adView.callToActionView = adView.findViewById(R.id.link_text)
        if (nativeAd.headline.isNullOrBlank()) {
            return null
        }
        if (nativeAd.callToAction.isNullOrBlank()) {
            return null
        }
        (adView.headlineView as TextView).text = nativeAd.headline
        setTextColor(adView.headlineView as TextView, titleColor)
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        adView.callToActionView.visibility = View.VISIBLE
        (adView.callToActionView as TextView).text = nativeAd.callToAction
        setTextColor(adView.callToActionView as TextView, linkToColor)
        setBgColor(adView.findViewById(R.id.linkto), linkToBgColor)
        if (nativeAd.icon == null || nativeAd.icon.drawable == null) {
            adView.iconView.visibility = View.GONE
            adView.findViewById<View>(R.id.icon_image_container).visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
        return adView
    }

    private fun setBgColor(view: View, bgColor: String) {
        if (bgColor.isNotEmpty()) {
            view.setBackgroundResource(R.drawable.round)
            val drawable = view.background as GradientDrawable
            drawable.setColor(Color.parseColor(bgColor))
        }
    }

    private fun setTextColor(view: TextView, textColor: String) {
        if (textColor.isNotEmpty()) {
            view.setTextColor(Color.parseColor(textColor))
        }
    }
}
