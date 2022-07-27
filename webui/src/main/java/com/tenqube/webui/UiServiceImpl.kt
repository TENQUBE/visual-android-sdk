package com.tenqube.webui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.tenqube.webui.component.ad.AdService
import com.tenqube.webui.component.bottomsheet.ItemListDialogFragment
import com.tenqube.webui.component.datepicker.DatePickerFragment
import com.tenqube.webui.component.datepicker.DatePickerListener
import com.tenqube.webui.component.datepicker.model.DateRequest
import com.tenqube.webui.component.timepicker.TimePickerFragment
import com.tenqube.webui.component.timepicker.TimePickerListener
import com.tenqube.webui.component.timepicker.model.TimeRequest
import com.tenqube.webui.dto.*

class UiServiceImpl(
    private val adService: AdService,
    private val activity: FragmentActivity,
    private val callback: (enabled: Boolean) -> Unit
) : UIService {

    private var audioManager: AudioManager? = null

    override fun finish() {
        activity.finish()
    }

    override fun onClickSound() {
        audioManager?.playSoundEffect(AudioManager.FX_KEY_CLICK)
    }

    override fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showDialog(request: ShowDialog) {
        val builder = AlertDialog.Builder(activity)

        builder
            .setTitle(request.request.title)
            .setMessage(request.request.message)
            .setPositiveButton(
                request.request.positive.button.text
            ) { _, _ ->
                request.callback.onClickPositiveButton()
            }
            .setNegativeButton(
                request.request.negative.button.text
            ) { _, _ ->
                request.callback.onCLickNegativeButton()
            }
            .create()
            .run {
                this.show()
                this.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                    setBackgroundColor(Color.parseColor(request.request.positive.button.bgColor))
                    setTextColor(Color.parseColor(request.request.positive.button.color))
                }

                this.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                    setBackgroundColor(Color.parseColor(request.request.negative.button.bgColor))
                    setTextColor(Color.parseColor(request.request.negative.button.color))
                }
            }
    }

    override fun openSelectBox(request: OpenSelectBox) {
        ItemListDialogFragment.newInstance(request)
            .show(activity.supportFragmentManager, "")
    }

    override fun openNewView(request: OpenNewViewDto) {
        when (request.type) {
            "internal" -> {
                val intent = Intent(this.activity, activity.javaClass)
                intent.putExtra("url", request.url)
                activity.startActivityForResult(intent, 0)
            }
            else -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(request.url)
                activity.startActivity(intent)
            }
        }
    }

    override fun openDatePicker(request: OpenDatePicker) {
        val newFragment = DatePickerFragment.newInstance(
            DateRequest(
                request.request.date,
                request.request.callbackJS
            )
        )
        newFragment.setListener(
            object : DatePickerListener {
                override fun onCalendar(date: String) {
                    request.callback(date)
                }
            }
        )
        newFragment.show(activity.supportFragmentManager, "datePicker")
    }

    override fun openTimePicker(request: OpenTimePicker) {
        val newFragment = TimePickerFragment.newInstance(
            TimeRequest(
                request.request.date
            )
        )
        newFragment.setListener(
            object : TimePickerListener {
                override fun onCalendar(time: String) {
                    request.callback(time)
                }
            }
        )
        newFragment.show(activity.supportFragmentManager, "timePicker")
    }

    override fun openNotiSettings() {
        val i = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        activity.startActivity(i)
//        val notiCatchDialogFragment = NotiCatchDialogFragment.newInstance(NotiCatchArg(listOf()))
//        notiCatchDialogFragment.setCallback(
//            callback = object : NotiCatchDialogFragment.Callback {
//                override fun onClickNext() {
//
//                }
//            }
//        )
//        notiCatchDialogFragment.show(activity.supportFragmentManager, "notiCatch")
    }

    override fun setRefreshEnabled(enabled: Boolean) {
        callback(enabled)
    }

    override fun showAd(request: ShowAd) {
        adService.showAd(request)
    }

    override fun hideAd() {
        adService.hideAd()
    }
}
