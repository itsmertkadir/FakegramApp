package com.mertkadir.fakegramapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mertkadir.fakegramapp.R
import com.mertkadir.fakegramapp.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {


    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.registerButton.setOnClickListener {

            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val passwordVerify = binding.editTextPassword2.text.toString()

            if (email.equals("") || password.equals("") || passwordVerify.equals("")){
                Toast.makeText(it.context, "Enter email , password and password verify", Toast.LENGTH_LONG).show()
            }else if (password != passwordVerify){
                Toast.makeText(it.context, "password and password verify do not match", Toast.LENGTH_LONG).show()
            }else{

                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {authResult ->


                    val intent = Intent(it.context,MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                }.addOnFailureListener {error ->
                    Toast.makeText(it.context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.loginTextView.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }

    }
}