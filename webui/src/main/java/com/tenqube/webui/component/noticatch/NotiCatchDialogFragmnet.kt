package com.tenqube.webui.component.noticatch

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tenqube.webui.R
import com.tenqube.webui.component.noticatch.dto.NotificationAppDto
import com.tenqube.webui.component.utils.Utils
import java.io.Serializable

class NotiCatchDialogFragment : DialogFragment() {
    private var appAdapter: AppAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var callback: Callback? = null
    private var arg: NotiCatchArg? = null
    val viewModel: NotiCatchViewModel by viewModels {
        NotiCatchViewModelFactory(ResourceAppService(requireContext()))
    }
    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onClickNext()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arg = arguments?.getSerializable("arg") as NotiCatchArg
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        try {
            val view: View = requireActivity().layoutInflater
                .inflate(R.layout.visual_dialog_noti_catch, null)
            val titleTextView = view.findViewById<TextView>(R.id.title)
            titleTextView.text = getString(
                R.string.visual_notification_catch_dialog_title,
                Utils.getApplicationName(
                    requireActivity().applicationContext
                )
            )
            dialog.setView(view, 0, 0, 0, 0)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCanceledOnTouchOutside(true)
            if (dialog.window != null) dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            recyclerView = view.findViewById(R.id.recyclerView)
            appAdapter =
                AppAdapter(
                    Glide.with(
                        requireActivity()
                    )
                )
            recyclerView?.layoutManager = LinearLayoutManager(activity)
            recyclerView?.adapter = appAdapter
            viewModel.apps.observe(this) {
                appAdapter?.submitList(it)
            }

            viewModel.loadApps()
            val nextButton = view.findViewById<Button>(R.id.next)
            nextButton.setOnClickListener {
                if (callback != null) {
                    callback?.onClickNext()
                }
                dismissAllowingStateLoss()
            }
        } catch (e: Exception) {
            dismissAllowingStateLoss()
        }

        return dialog
    }

    companion object {
        fun newInstance(param: NotiCatchArg): NotiCatchDialogFragment {
            return NotiCatchDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("arg", param)
                }
            }
        }
    }
}

data class NotiCatchArg(val apps: List<NotificationAppDto>) : Serializable
