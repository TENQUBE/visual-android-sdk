package com.tenqube.webui.component.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tenqube.webui.dto.SelectBoxItem

class ItemAdapter internal constructor(private val selectedColor: Int,
                                       private val items: List<SelectBoxItem>,
                                       private var callback: (SelectBoxItem) -> Unit) :
    RecyclerView.Adapter<BottomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomViewHolder {
        return BottomViewHolder(LayoutInflater.from(parent.context), parent, selectedColor) {
            if(it != RecyclerView.NO_POSITION && items.size > it) {
                callback(items[it])
            }
        }
    }

    override fun onBindViewHolder(holder: BottomViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}