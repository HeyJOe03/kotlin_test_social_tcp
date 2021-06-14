package org.altervista.carminati.chat_app_first.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.altervista.carminati.chat_app_first.R
import org.altervista.carminati.chat_app_first.data.Message
import org.altervista.carminati.chat_app_first.utils.Constants.RECEIVE_ID
import org.altervista.carminati.chat_app_first.utils.Constants.SEND_ID
import org.altervista.carminati.chat_app_first.utils.Time

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var socket : Socket
    private lateinit var adapter: MessagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycleView()   //set the recycleView
        btn_send.setOnClickListener{sendMessage()} //set the function to send messages

        try{
            socket = IO.socket("http://192.168.1.139:3000")
            socket.on(Socket.EVENT_CONNECT){
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
                    //btn_join_chat.text = socket.id()
                }
            }
            socket.connect()
        }
        catch (e:Exception){
            Toast.makeText(this@MainActivity, "" + e.message, Toast.LENGTH_SHORT).show()
        }

        socket.on("message"){
            args ->
            runOnUiThread {
                //Toast.makeText(this@MainActivity, args[0].toString(), Toast.LENGTH_SHORT).show()
                val response = JSONObject(args[0].toString())
                val message = response.get("message").toString()
                val sender = response.get("sender").toString()
                val to = response.get("to")
                val builder = StringBuilder()
                    .append(message)
                    .append('\n')
                    .append(sender)
                    .append('\n')
                    .append(to)
                customMessage(message,sender)
            }
        }


    }

    private fun customMessage(message: String,sender: String){
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val timestamp = Time.timeStamp()
                adapter.insertMessage(Message(message, sender,timestamp))

                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }

        }
    }

    private fun recycleView(){
        adapter = MessagingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun sendMessage(){
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if(message.isNotEmpty()){
            et_message.setText("")

            adapter.insertMessage(Message(message, SEND_ID,timeStamp))

            rv_messages.scrollToPosition(adapter.itemCount-1)
        }

        val json = JSONObject()
        json.put("sender",socket.id())
        json.put("to","all")
        json.put("message",message)
        socket.emit("message",json)
    }

}