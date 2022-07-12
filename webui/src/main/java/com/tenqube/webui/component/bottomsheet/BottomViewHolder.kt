package com.tenqube.webui.component.bottomsheet

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tenqube.webui.R
import com.tenqube.webui.dto.SelectBoxItem

class BottomViewHolder internal constructor(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val selectedColor: Int,
    val callback: (Int) -> Unit
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.fragment_item_list_dialog_list_dialog_item,
        parent,
        false
    )
) {

    var container: LinearLayout = itemView.findViewById(R.id.container)
    var name: TextView = itemView.findViewById(R.id.name)

    init {
        itemView.setOnClickListener {
            callback(adapterPosition)
        }
    }

    fun bind(bottomSheetData: SelectBoxItem) {

        when (bottomSheetData.isSelected) {
            true -> {
                changeColor(container, selectedColor)
                name.setTextColor(selectedColor)
            }
            false -> {
                changeColor(
                    container,
                    ContextCompat.getColor(itemView.context, R.color.colorPopupGrey)
                )
                name.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
            }
        }
        name.text = bottomSheetData.name
    }

    private fun changeColor(container: LinearLayout, color: Int) {
        val background = container.background
        if (background is GradientDrawable) {
            background.setStroke(3, color)
        }
    }
}
