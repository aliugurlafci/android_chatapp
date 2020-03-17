package com.example.chatapp.ui.main.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatapp.R
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.ui.main.MessageClass
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.ui.main.PageViewModel
import com.example.chatapp.ui.main.Sezar.SezarGet
import com.example.chatapp.ui.main.Sezar.SezarPost

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}

class ChatAdapter(private val context: Context, val msgList: MutableList<MessageClass?>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    inner class getMessagesViewHolder(itemView: View) : BaseViewHolder<MessageClass?>(itemView) {
        override fun bind(item: MessageClass?) {
            val gelenMesaj = itemView.findViewById(R.id.gelen_mesaj) as TextView
            val gelenInfo = itemView.findViewById(R.id.gelen_info) as TextView
            gelenMesaj.text = SezarGet(item?.mesaj).toMessage()
            gelenInfo.text = SezarGet(item?.username).toMessage()
        }
    }

    inner class postMessagesViewHolder(itemView: View) : BaseViewHolder<MessageClass?>(itemView) {
        override fun bind(item: MessageClass?) {
            val gdnMesaj = itemView.findViewById(R.id.giden_mesaj) as TextView
            val gdnInfo = itemView.findViewById(R.id.message_post_info) as TextView
            gdnMesaj.text = SezarGet(item?.mesaj).toMessage()
            gdnInfo.text = SezarGet(item?.username).toMessage()

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            GET_VIEW -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.get_message_text_container, parent, false)
                getMessagesViewHolder(view)
            }
            POST_VIEW -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.post_message_text_container, parent, false)
                postMessagesViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.get_message_text_container, parent, false)
                postMessagesViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = msgList[position]
        when (holder) {
            is getMessagesViewHolder -> holder.bind(element as MessageClass)
            is postMessagesViewHolder -> holder.bind(element as MessageClass)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val sender = msgList[position]?.metod
        val x = PageViewModel.getUserUI().value
        return if (sender.equals("post")) POST_VIEW else GET_VIEW
    }

    companion object {
        private const val GET_VIEW = 1
        private const val POST_VIEW = 2
    }
}
