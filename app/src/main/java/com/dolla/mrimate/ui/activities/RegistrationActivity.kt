package com.dolla.mrimate.ui.activities

import android.os.Bundle
import com.dolla.mrimate.databinding.ActivityRegistrationBinding

class RegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}