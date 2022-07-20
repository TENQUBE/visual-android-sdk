package com.tenqube.ibk.progress

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.tenqube.ibk.R

class VisualProgressDialogFragment : DialogFragment() {
    private var progressBar: ProgressBar? = null
    private var percentTextView: TextView? = null

    @SuppressLint("SetTextI18n")
    fun setProgressValue(now: Int, total: Int) {
        val progress = now * 100 / total
        progressBar?.progress = progress
        percentTextView?.text = "$progress%"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog))
        val view: View =
            requireActivity().layoutInflater.inflate(R.layout.visual_dialog_progress, null)
        progressBar = view.findViewById<View>(R.id.visual_progress) as ProgressBar
        percentTextView = view.findViewById<View>(R.id.visual_percent) as TextView
        builder.setView(view)
        val dialog: Dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        if (dialog.window != null) dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    companion object {
        fun newInstance(): VisualProgressDialogFragment {
            return VisualProgressDialogFragment()
        }
    }
}