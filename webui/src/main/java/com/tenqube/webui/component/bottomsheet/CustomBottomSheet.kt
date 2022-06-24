package com.tenqube.webui.component.bottomsheet

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tenqube.webui.R
import com.tenqube.webui.component.bottomsheet.model.OpenSelectBoxItem
import com.tenqube.webui.component.bottomsheet.model.OpenSelectBoxRequest
import com.tenqube.webui.component.utils.Utils

class CustomBottomSheet(
    private val activity: Activity
) {
    private var bottomListener: OnBottomListener? = null
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var peekHeight = 0

    interface OnBottomListener {
        fun onItemSelected(openSelectBoxItem: OpenSelectBoxItem)
    }

    fun setBottomListener(bottomListener: OnBottomListener?) {
        this.bottomListener = bottomListener
        peekHeight = Utils.getDeviceHeight(activity) / 2 - Utils.dpToPx(24)
    }

    fun showBottomDialog(selectBoxRequest: OpenSelectBoxRequest?) {
        try {
            if (selectBoxRequest == null) return
            if (bottomSheetDialog == null) {
                bottomSheetDialog = BottomSheetDialog(activity).apply {
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                }

            }
            bottomSheetDialog?.let { dialog ->
                if (!isShowing) {
                    val view = activity.layoutInflater.inflate(R.layout.visual_bottom_sheet_view, null)
                    if (view != null) {
                        val titleTextView = view.findViewById<View>(R.id.title) as TextView
                        titleTextView.text = selectBoxRequest.title
                        view.findViewById<View>(R.id.close).setOnClickListener { dismiss() }
                        val recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
                        if (selectBoxRequest.list.size > 3) {
                            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            params.height = peekHeight
                            recyclerView.layoutParams = params
                        }
                        val linearLayoutManager = LinearLayoutManager(activity)
                        recyclerView.layoutManager = linearLayoutManager
                        recyclerView.setHasFixedSize(true)
                        recyclerView.adapter = BottomSheetAdapter(selectBoxRequest, bottomListener)
                        dialog.setContentView(view)
                        val mBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(view.parent as View)
                        mBehavior.peekHeight = peekHeight
                        dialog.show()
                    }
                }
            }

        } catch (e: Exception) {
        }
    }

    fun dismiss() {
        if (isShowing) {
            bottomSheetDialog?.dismiss()
        }
    }

    private val isShowing: Boolean
        get() = bottomSheetDialog != null && bottomSheetDialog!!.isShowing
}