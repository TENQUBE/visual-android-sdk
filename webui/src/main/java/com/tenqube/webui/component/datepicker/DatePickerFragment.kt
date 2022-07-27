package com.tenqube.webui.component.datepicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.tenqube.webui.R
import com.tenqube.webui.component.datepicker.model.DateRequest
import com.tenqube.webui.component.utils.Utils
import java.util.*

class DatePickerFragment : DialogFragment(), OnDateSetListener {
    private var listener: DatePickerListener? = null
    lateinit var calendar: Calendar
    lateinit var dateRequest: DateRequest

    fun setListener(listener: DatePickerListener) {
        this.listener = listener
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        dateRequest.callbackJS?.let {
            calendar[year, month] = day
            onCalendar(Utils.getYMD(calendar))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            val request = (arguments.getSerializable(ARG_CAL) as DateRequest)
            val calendarYMD = Utils.toCalendarYMD(request.date)
            if (calendarYMD == null) dismiss()
            dateRequest = request
            calendar = calendarYMD!!
        } ?: dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the date picker
        activity?.let {
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(it, R.style.PickerTheme, this, year, month, day)
            val datePicker = dialog.datePicker
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = 2018
            calendar[Calendar.MONTH] = 0
            calendar[Calendar.DATE] = 1
            datePicker.minDate = calendar.timeInMillis
            calendar[Calendar.YEAR] = year + 10
            datePicker.maxDate = calendar.timeInMillis
            return dialog
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    fun onCalendar(date: String) {
        if (listener != null) {
            listener!!.onCalendar(date)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        private const val ARG_CAL = "request"
        fun newInstance(dateRequest: DateRequest): DatePickerFragment {
            val fragment = DatePickerFragment()
            val args = Bundle()
            args.putSerializable(ARG_CAL, dateRequest)
            fragment.arguments = args
            return fragment
        }
    }
}
