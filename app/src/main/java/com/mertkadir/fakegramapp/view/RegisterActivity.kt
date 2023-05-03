package com.mertkadir.fakegramapp.view

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mertkadir.fakegramapp.R
import com.mertkadir.fakegramapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Init
        auth = Firebase.auth

        binding.fragmentContainerView.visibility = View.GONE
        binding.textView2.visibility = View.VISIBLE

    }

    fun registerBTN(view: View){


        val emailText = binding.editTextEmail.text.toString()
        val passwordText = binding.editTextPassword.text.toString()
        val passwordVerifyText = binding.editTextPasswordVerify.text.toString()

        if (emailText.equals("") || passwordText.equals("") ||passwordVerifyText.equals("")){
            Toast.makeText(this, "Enter Email and Password-passVerify", Toast.LENGTH_SHORT).show()
        }else if (passwordText != passwordVerifyText){
            Toast.makeText(this, "password do not match", Toast.LENGTH_SHORT).show()
        }else {

            auth.createUserWithEmailAndPassword(emailText,passwordText).addOnSuccessListener {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }

    }

    fun login(view: View){

        binding.fragmentContainerView.visibility = View.VISIBLE
        binding.registerLinearLayout.visibility = View.GONE
        binding.textView2.visibility = View.GONE

    }

}