package com.tenqube.webui.component.bottomsheet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tenqube.webui.R
import com.tenqube.webui.component.bottomsheet.model.OpenSelectBoxRequest

class BottomSheetAdapter internal constructor(
    selectBoxRequests: OpenSelectBoxRequest,
    private val bottomListener: CustomBottomSheet.OnBottomListener?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BottomSheetListener {
    private val selectBoxInfos = selectBoxRequests.list
    private var selectedPos = -1
    private var selectedColor = 0
    private fun setSelectedColor(color: String) {
        selectedColor = try {
            Color.parseColor(color)
        } catch (e: Exception) {
            Color.parseColor("#ff051d")
        }
    }

    private fun setSelectedPos() {
        val size = selectBoxInfos.size
        for (i in 0 until size) {
            if (selectBoxInfos[i].isSelected) {
                selectedPos = i
                return
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.visual_bottom_sheet_view, parent, false) as ViewGroup
        return BottomSheetViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            val selectBoxInfo = selectBoxInfos[position]
            selectBoxInfo.isSelected = selectedPos == position
            val bHolder = holder as BottomSheetViewHolder
            bHolder.onBind(selectBoxInfo, selectedColor)
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return selectBoxInfos.size
    }

    override fun onClick(position: Int) {
        selectedPos = position
        notifyItemChanged(position)
        bottomListener?.onItemSelected(selectBoxInfos[position])
    }

    init {
        setSelectedColor(selectBoxRequests.selectColor)
        setSelectedPos()
    }
}