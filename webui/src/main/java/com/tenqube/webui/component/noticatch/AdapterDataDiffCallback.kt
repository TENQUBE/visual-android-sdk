package com.tenqube.webui.component.noticatch

import androidx.recyclerview.widget.DiffUtil
import com.tenqube.webui.component.noticatch.dto.NotificationAppDto

class AdapterDataDiffCallback : DiffUtil.ItemCallback<NotificationAppDto>() {
    override fun areItemsTheSame(oldItem: NotificationAppDto, newItem: NotificationAppDto): Boolean {
        return oldItem.name == newItem.name && oldItem.image == newItem.image
    }

    override fun areContentsTheSame(oldItem: NotificationAppDto, newItem: NotificationAppDto): Boolean {
        return oldItem == newItem
    }
}
