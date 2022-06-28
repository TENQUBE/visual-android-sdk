package com.tenqube.webui.component.timepicker

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.tenqube.webui.R
import com.tenqube.webui.component.timepicker.model.TimeRequest
import com.tenqube.webui.component.utils.Utils
import java.lang.RuntimeException
import java.util.*

class TimePickerFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var listener: TimePickerListener? = null
    private var calendar = Calendar.getInstance()
    lateinit var timeRequest: TimeRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            val request = (arguments.getSerializable(ARG_CAL) as TimeRequest)
            val calendarHMS = Utils.toCalendarHMS(timeRequest.time)
            if (calendarHMS == null) dismiss()
            timeRequest = request
            calendar = calendarHMS!!
        }?: dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        return TimePickerDialog(
            activity, R.style.PickerTheme, this, hour, minute,
            DateFormat.is24HourFormat(activity)
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar[Calendar.HOUR_OF_DAY] = hourOfDay
        calendar[Calendar.MINUTE] = minute
        onTimeCalendar(Utils.getHMS(calendar))
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (context is TimePickerListener) {
            listener =
                context as TimePickerListener
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    fun setListener(listener: TimePickerListener) {
        this.listener = listener
    }

    fun onTimeCalendar(time: String){
        listener?.onCalendar(time)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        const val ARG_CAL = "calendar"
        fun newInstance(timeRequest: TimeRequest): TimePickerFragment {
            val fragment = TimePickerFragment()
            val args = Bundle()
            args.putSerializable(ARG_CAL, timeRequest)
            fragment.arguments = args
            return fragment
        }
    }
}