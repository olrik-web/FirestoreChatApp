package com.example.firestorechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private val firebaseController: FirebaseController = FirebaseController()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            createAccount()
        }

        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        btnSignIn.setOnClickListener {
            signIn()
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseController.getUser()
        if (currentUser != null) {
            Log.d(TAG, "currentUser exists ${currentUser.email}")
            val intent = Intent(this, ChatPage::class.java)
            startActivity(intent)
        }
    }


    private fun createAccount() {
        loginEmail = findViewById(R.id.textEmailAddress)
        loginPassword = findViewById(R.id.textPassword)
        val email = loginEmail.text.toString().trim()
        val password = loginPassword.text.toString().trim()

        if (credentialsFalse(email, password)) {
            Toast.makeText(
                baseContext, "Missing credentials", Toast.LENGTH_SHORT
            ).show()
            return
        } else {
            firebaseController.createUser(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmailAndPassword:success")
                        Toast.makeText(
                            baseContext, "Account Created.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.w(TAG, "createUserWithEmailAndPassword:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun signIn() {
        loginEmail = findViewById<EditText>(R.id.textEmailAddress)
        loginPassword = findViewById<EditText>(R.id.textPassword)
        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()

        if (credentialsFalse(email,password)) {
            Toast.makeText(
                baseContext, "Missing credentials", Toast.LENGTH_SHORT
            ).show()
        } else {
            firebaseController.signIn(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val intent = Intent(this, ChatPage::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            baseContext, "Sign In successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

    private fun credentialsFalse(email: String, password: String) : Boolean{
        return (email.isEmpty() || password.isEmpty())
    }


    companion object {
        private const val TAG = "EmailPassword"
    }
}


