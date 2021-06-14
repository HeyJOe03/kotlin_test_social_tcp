package org.altervista.carminati.chat_app_first.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.altervista.carminati.chat_app_first.data.Message
import androidx.recyclerview.widget.RecyclerView
import org.altervista.carminati.chat_app_first.R
import org.altervista.carminati.chat_app_first.utils.Constants.SEND_ID

import kotlinx.android.synthetic.main.message_item.view.* //fast way to access ui but deprecated
import org.altervista.carminati.chat_app_first.utils.Constants.RECEIVE_ID

class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    val messageList = mutableListOf<Message>()

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init{
            itemView.setOnClickListener{
                messageList.removeAt(adapterPosition) //to delete the message clicked
                notifyItemRemoved(adapterPosition) //animation for deletes
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item,parent,false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        //which side to display the message
        val currentMessage = messageList[position]

        //correct appearence of the message
        when(currentMessage.id){
            SEND_ID ->{
                //todo: controllare implementazione koltin-android-extensions
                holder.itemView.tv_message.apply{
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.itemView.tv_bot_message.visibility = View.GONE
            }

            RECEIVE_ID ->{
                holder.itemView.tv_bot_message.apply{
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.itemView.tv_message.visibility = View.GONE
            }
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun insertMessage(message: Message){
        this.messageList.add(message)
        notifyItemInserted(messageList.size) //move to the bottom
        notifyDataSetChanged()
    }
}