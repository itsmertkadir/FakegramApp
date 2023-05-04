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
import com.mertkadir.fakegramapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginbutton.setOnClickListener {
            val emilText = binding.editTextEmailLogin.text.toString()
            val passwordText = binding.editTextPasswordLogin.text.toString()


            auth.signInWithEmailAndPassword(emilText,passwordText).addOnSuccessListener {

                val intent = Intent(view.context,MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }.addOnFailureListener {
                Toast.makeText(view.context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }

        binding.register.setOnClickListener {

            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            Navigation.findNavController(it).navigate(action)

        }

        binding.resetPasswordText.setOnClickListener {

            val action = LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
            Navigation.findNavController(it).navigate(action)

        }

    }

}