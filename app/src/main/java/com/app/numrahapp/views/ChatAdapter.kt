package com.app.numrahapp.views
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.numrahapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(private val items: List<ChatModel>) :
    RecyclerView.Adapter<ChatAdapter.StringViewHolder>() {

    class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvText: TextView = itemView.findViewById(R.id.tv_text)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val ivTick: ImageView = itemView.findViewById(R.id.iv_tick)
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isOther) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val layoutId = if (viewType == 0) {
            R.layout.item_chat_left
        } else {
            R.layout.item_chat_right
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return StringViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.tvText.text = items[position].content
        holder.tvTime.text = formatTime(items[position].time)
        if (getItemViewType(position)==1){
            holder.ivTick.visibility=View.VISIBLE
            if (items[position].isSeen){
                holder.ivTick.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_double_tick))
            }else{
                holder.ivTick.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_tick))
            }
        }else{
            holder.ivTick.visibility=View.GONE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

