package org.altervista.carminati.chat_app_first.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.altervista.carminati.chat_app_first.R
import org.altervista.carminati.chat_app_first.data.Message
import org.altervista.carminati.chat_app_first.utils.Constants.RECEIVE_ID
import org.altervista.carminati.chat_app_first.utils.Constants.SEND_ID
import org.altervista.carminati.chat_app_first.utils.Time

//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var socket : Socket
    private lateinit var adapter: MessagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycleView()

        customMessage("hello")
        customMessage("how are you?")

        btn_send.setOnClickListener{sendMessage()}

        /*
        val btn_send = findViewById<Button>(R.id.btn_send)
        val btn_join_chat = findViewById<Button>(R.id.btn_join_chat)
        val edt_message = findViewById<EditText>(R.id.edt_message)
        val edt_to = findViewById<EditText>(R.id.edt_to)
        val edt_username = findViewById<EditText>(R.id.edt_username)
        val txt_message = findViewById<TextView>(R.id.txt_message)

        try{
            socket = IO.socket("http://192.168.1.139:3000")
            socket.on(Socket.EVENT_CONNECT){
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
                    btn_join_chat.text = socket.id()
                }
            }
            socket.connect()
        }
        catch (e:Exception){
            Toast.makeText(this@MainActivity, "" + e.message, Toast.LENGTH_SHORT).show()
        }


        btn_send.setOnClickListener {
            val message = edt_message.text.toString()
            if(message != "") {
                val json = JSONObject()
                json.put("sender",edt_username.text.toString())
                json.put("to",edt_to.text.toString())
                json.put("message",message)
                socket.emit("message",json)
            } else {
                Toast.makeText(this@MainActivity,"Write some text to send",Toast.LENGTH_SHORT).show()
            }
            edt_message.text.clear()
        }

        btn_join_chat.setOnClickListener{
            if(edt_username.text.toString() != ""){

                socket.emit("join_chat", edt_username.text.toString())

                edt_to.visibility = EditText.VISIBLE
                edt_message.visibility = EditText.VISIBLE
                btn_send.visibility = Button.VISIBLE
                txt_message.visibility = TextView.VISIBLE
                btn_join_chat.visibility = Button.GONE

                edt_username.keyListener = null
            }
        }

        socket.on("message"){
            args ->
            runOnUiThread {
                //Toast.makeText(this@MainActivity, args[0].toString(), Toast.LENGTH_SHORT).show()
                val response = JSONObject(args[0].toString())
                val message = response.get("message")
                val sender = response.get("sender")
                val to = response.get("to")
                val builder = StringBuilder()
                    .append(message)
                    .append('\n')
                    .append(sender)
                    .append('\n')
                    .append(to)
                txt_message.text = builder.toString()
            }
        }
         */

    }

    private fun customMessage(message: String){
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val timestamp = Time.timeStamp()
                adapter.insertMessage(Message(message, RECEIVE_ID,timestamp))

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
    }

}