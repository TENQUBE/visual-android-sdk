package com.tenqube.webui.component.noticatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.tenqube.webui.R
import com.tenqube.webui.component.noticatch.dto.NotificationAppDto

class AppAdapter (private val requestManager: RequestManager,
                  private val apps: List<NotificationAppDto>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class NotiCatchViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById<View>(R.id.icon) as ImageView
        val appName: TextView = itemView.findViewById<View>(R.id.app_name) as TextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotiCatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.visual_item_noti, parent, false)

        return NotiCatchViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notiCatch: NotificationAppDto = apps[position]
        val notiCatchViewHolder = holder as NotiCatchViewHolder
        requestManager.load(notiCatch.image).into(notiCatchViewHolder.icon)
        notiCatchViewHolder.appName.text = notiCatch.name
    }

    override fun getItemCount(): Int {
        return apps.size
    }
}
