package com.dolla.mrimate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dolla.mrimate.databinding.ActivityRegistrationBinding
import com.dolla.mrimate.signin.SignInActivity
import com.dolla.mrimate.signup.SignUpActivity
import com.dolla.mrimate.util.setContentViewCustom

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentViewCustom(binding.root)
    }

    override fun onStart() {
        super.onStart()
        // set the behavior of buttons
        binding.btSignIn.setOnClickListener {
            navigateToRequiredActivity(SignInActivity::class.java)
        }

        binding.btCreateAccount.setOnClickListener {
            navigateToRequiredActivity(SignUpActivity::class.java)
        }
    }

    private fun navigateToRequiredActivity(destination: Class<*>) {
        startActivity(Intent(this@RegistrationActivity, destination))
    }
}