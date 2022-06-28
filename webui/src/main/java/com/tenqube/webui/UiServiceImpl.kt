package com.tenqube.webui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tenqube.webui.component.bottomsheet.CustomBottomSheet
import com.tenqube.webui.component.bottomsheet.model.OpenSelectBoxItem
import com.tenqube.webui.component.bottomsheet.model.OpenSelectBoxRequest
import com.tenqube.webui.component.datepicker.DatePickerFragment
import com.tenqube.webui.component.datepicker.model.DateRequest
import com.tenqube.webui.component.dialog.DialogCallback
import com.tenqube.webui.component.timepicker.TimePickerFragment
import com.tenqube.webui.component.timepicker.model.TimeRequest
import com.tenqube.webui.dto.*

class UiServiceImpl(
    private val activity: AppCompatActivity,
    private val webView: WebView
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

    override fun showDialog(request: ShowDialogDto, callback: DialogCallback) {
        val builder = AlertDialog.Builder(activity)

        builder
            .setTitle(request.title)
            .setMessage(request.message)
            .setPositiveButton(
                request.positive.button.text
            ) { _, _ ->
                callback.onClickPositiveButton()
            }
            .setNegativeButton(
                request.negative.button.text
            ) { _, _ ->
                callback.onCLickNegativeButton()
            }
            .create()
            .run {
                this.show()
                this.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                    setBackgroundColor(Color.parseColor(request.positive.button.bgColor))
                    setTextColor(Color.parseColor(request.positive.button.color))
                }

                this.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                    setBackgroundColor(Color.parseColor(request.negative.button.bgColor))
                    setTextColor(Color.parseColor(request.negative.button.color))
                }
            }
    }

    override fun showSelectBox(request: ShowSelectBoxDto) {
        val bottomSheet = CustomBottomSheet(activity)
        bottomSheet.showBottomDialog(
            OpenSelectBoxRequest(
                request.title,
                request.selectedColor,
                request.data.map {
                    OpenSelectBoxItem(
                        it.name,
                        it.orderByType,
                        it.isSelected
                    )
                }
        ))
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

    override fun showDatePicker(request: ShowDatePickerDto) {
        val newFragment = DatePickerFragment.newInstance(
            DateRequest(
                request.date,
                request.callbackJS
            )
        )
        newFragment.show(activity.supportFragmentManager, "datePicker")
    }

    override fun showTimePicker(request: ShowTimePickerDto) {
        val newFragment = TimePickerFragment.newInstance(
            TimeRequest(
                request.date,
                request.callbackJS
            )
        )
        newFragment.show(activity.supportFragmentManager, "timePicker")
    }
}