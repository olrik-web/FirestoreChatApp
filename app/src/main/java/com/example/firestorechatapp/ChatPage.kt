package com.example.firestorechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat_page.*

class ChatPage : AppCompatActivity() {

    private val firebaseController: FirebaseController = FirebaseController()
    private var messageList: List<MessageModel> = ArrayList()
    private val messageListAdapter: MessageListAdapter = MessageListAdapter(messageList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        signOut.setOnClickListener() {
            firebaseController.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        sendMessage.setOnClickListener() {
            val messageText = inputMessage.text.toString()
            saveMessage(messageText)
            inputMessage.setText("")
        }

        //Init recycler view
        messageListRecycler.layoutManager = LinearLayoutManager(this)
        messageListRecycler.adapter = messageListAdapter

        loadMessages()
    }

    private fun loadMessages() {
        firebaseController.getMessages().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.w(TAG, "loadMessages:success")
                messageList = task.result!!.toObjects(MessageModel::class.java)
                messageListAdapter.messageListItems = messageList
                messageListAdapter.notifyDataSetChanged()
            } else {
                Log.w(TAG, "loadMessages:failure", task.exception)
            }
        }
    }

    private fun saveMessage(messageText: String) {
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Type message", Toast.LENGTH_SHORT).show()
            return
        }else {
            firebaseController.sendMessage(messageText).addOnSuccessListener {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
                loadMessages()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Message failed to be sent", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "loadMessages:failure", it.cause)
                }
        }

    }


    companion object {
        private const val TAG = "ChatPage"
    }
}