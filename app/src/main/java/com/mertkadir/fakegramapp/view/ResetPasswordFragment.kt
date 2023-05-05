package com.mertkadir.fakegramapp.view

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
import com.mertkadir.fakegramapp.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment() {

    private lateinit var binding : FragmentResetPasswordBinding
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.resetPasswordButton.setOnClickListener {
            val email = binding.editTextEmailReset.text.toString()
            if (!email.equals("")) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(view.context, "Sent Email!!", Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(view.context, it.exception!!.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                    }

                }

            }else{
                Toast.makeText(it.context, "Enter Email!!", Toast.LENGTH_SHORT).show()
            }

        }

        binding.backLoginScreen.setOnClickListener {
            val action = ResetPasswordFragmentDirections.actionResetPasswordFragmentToLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }

    }

}