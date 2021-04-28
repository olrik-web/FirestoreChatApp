package com.example.firestorechatapp

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FirebaseController {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    //Auth
    fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun createUser(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    fun signIn(email: String, password: String): Task<AuthResult> {
     return firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    //Firestore
    //Todo:orderby date
    fun getMessages(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("messages")
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
    }

    fun sendMessage(messageText: String): Task<DocumentReference> {
        val message: MutableMap<String, Any> = HashMap()
        message["messageText"] = messageText
        message["sender"] = firebaseAuth.currentUser.email
        message["date"] = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())

        return firebaseFirestore.collection("messages")
            .add(message)
    }
    companion object {
        private const val TAG = "EmailPassword"
    }
}