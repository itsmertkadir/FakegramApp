package com.mertkadir.fakegramapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mertkadir.fakegramapp.R
import com.mertkadir.fakegramapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}