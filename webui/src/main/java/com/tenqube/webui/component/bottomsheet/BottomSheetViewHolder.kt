package com.tenqube.webui.component.bottomsheet

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tenqube.webui.R
import com.tenqube.webui.component.bottomsheet.model.OpenSelectBoxItem
import com.tenqube.webui.component.utils.Utils

class BottomSheetViewHolder(
    convertView: View,
    onClickListener: BottomSheetListener?
) : RecyclerView.ViewHolder(convertView) {

    var container: LinearLayout
    var nameTextView: TextView

    fun onBind(
        selectBoxInfo: OpenSelectBoxItem,
        selectedColor: Int
    ) {
        if (selectBoxInfo.isSelected) {
            Utils.changeColor(container, selectedColor)
            nameTextView.setTextColor(selectedColor)
        } else {
            Utils.changeColor(container, ContextCompat.getColor(nameTextView.context, R.color.colorPopupGrey))
            nameTextView.setTextColor(nameTextView.context.resources.getColor(android.R.color.black))
        }
        nameTextView.text = selectBoxInfo.name
    }

    init {
        convertView.setOnClickListener { if (onClickListener != null && adapterPosition != RecyclerView.NO_POSITION) onClickListener.onClick(adapterPosition) }
        container = convertView.findViewById<View>(R.id.container) as LinearLayout
        nameTextView = convertView.findViewById<View>(R.id.name) as TextView
    }
}
