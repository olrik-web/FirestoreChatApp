package com.example.firestorechatapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chat_page.view.*
import kotlinx.android.synthetic.main.list_item_received.view.*
import kotlinx.android.synthetic.main.list_item_received.view.messageSender
import kotlinx.android.synthetic.main.list_item_sent.view.*

private const val MESSAGE_TYPE_RECEIVED: Int = 0
private const val MESSAGE_TYPE_SENT: Int = 1

class MessageListAdapter(var messageListItems: List<MessageModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val firebaseController: FirebaseController = FirebaseController()

    class MessageSentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(messageModel: MessageModel) {
            itemView.sentMessage.text = messageModel.messageText
            itemView.sentDate.text = messageModel.date.toString()
        }
    }

    class MessageReceivedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(messageModel: MessageModel) {
            itemView.receivedMessage.text = messageModel.messageText
            itemView.messageSender.text = messageModel.sender
            itemView.receivedDate.text = messageModel.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MESSAGE_TYPE_RECEIVED) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_received, parent, false)
            MessageReceivedViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_sent, parent, false)
            MessageSentViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return messageListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MESSAGE_TYPE_RECEIVED) {
            (holder as MessageReceivedViewHolder).bind(messageListItems[position])
        } else {
            (holder as MessageSentViewHolder).bind(messageListItems[position])
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(messageListItems[position].sender == firebaseController.getUser()?.email) {
            MESSAGE_TYPE_SENT
        } else {
            MESSAGE_TYPE_RECEIVED
        }
    }
    companion object {
        private const val TAG = "Adapter"
    }


}