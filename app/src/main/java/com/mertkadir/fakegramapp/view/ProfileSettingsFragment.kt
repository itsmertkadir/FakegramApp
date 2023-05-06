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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mertkadir.fakegramapp.R
import com.mertkadir.fakegramapp.databinding.FragmentProfileSettingsBinding
import com.mertkadir.fakegramapp.model.Posts

class ProfileSettingsFragment : Fragment() {

    private lateinit var binding : FragmentProfileSettingsBinding
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exitSettings.setOnClickListener {
            val action = ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToProfileFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.signOut.setOnClickListener{

        }


    }



}