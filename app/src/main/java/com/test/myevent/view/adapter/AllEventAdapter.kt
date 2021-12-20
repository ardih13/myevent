package com.test.myevent.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.myevent.R
import com.test.myevent.data.model.EventModel
import com.test.myevent.util.DateUtil

class AllEventAdapter(
    val context: Context,
    private val listAllEvent: ArrayList<EventModel>
) : RecyclerView.Adapter<AllEventAdapter.Companion.ViewHolder>() {

    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageEvent: ImageView = itemView.findViewById(R.id.ivEvent)
            val titleEvent: TextView = itemView.findViewById(R.id.tvTitleEvent)
            val dateEvent: TextView = itemView.findViewById(R.id.tvDateEvent)
            val roomEvent: TextView = itemView.findViewById(R.id.tvRoomEvent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_event, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listAllEvent.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val event = listAllEvent[position]

        Glide.with(context)
            .load(event.image)
            .centerCrop()
            .placeholder(R.drawable.ic_no_image)
            .into(holder.imageEvent)

        holder.titleEvent.text = event.name

        val startDate = DateUtil.simplifyDateFormat(
            event.start_date,
            "yyyy-MM-dd",
            "dd MMM yyyy"
        )
        val startTime = DateUtil.simplifyDateFormat(
            event.start_time,
            "hh:mm",
            "hh:mm a"
        )
        holder.dateEvent.text = """$startDate - $startTime"""
        holder.roomEvent.text = event.location

    }

}