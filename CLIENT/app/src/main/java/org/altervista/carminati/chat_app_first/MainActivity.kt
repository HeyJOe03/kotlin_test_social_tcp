package org.altervista.carminati.chat_app_first

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var socket : Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val btn_send = findViewById<Button>(R.id.btn_send)
        //val txt_message = findViewById<TextView>(R.id.txt_message)
        //val edt_message = findViewById<EditText>(R.id.edt_message)

        try{
            socket = IO.socket("http://192.168.1.139:3000")
            socket.on(Socket.EVENT_CONNECT){
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
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
                json.put("sender","Android APP")
                json.put("to","server")
                json.put("message",message)
                socket.emit("message",json)
            } else {
                Toast.makeText(this@MainActivity,"Write some text to send",Toast.LENGTH_SHORT).show()
            }
            edt_message.text.clear()
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

    }
}