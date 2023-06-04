package com.dolla.mrimate.resetpassword

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dolla.mrimate.databinding.ActivityResetPasswordBinding
import com.dolla.mrimate.util.*


class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentViewCustom(binding.root)

        // set the view model
        viewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        // observe progress bar and reset password layout visibility
        observeResettingPasswordState()
        // error message visibility
        observeErrorMessageState()
        // toast message visibility
        observeToastMessageState()
        // observe the sign in navigation state
        observeNavigationToSignInState()
        // back button click listener
        onBackClicked()
        // reset password button click listener
        onResetPasswordClicked()
    }

    private fun observeResettingPasswordState() {
        // reset password views visibility when loading
        viewModel.userResetPasswordStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = VISIBLE
                    binding.tvEmail.visibility = INVISIBLE
                    binding.etEmail.visibility = INVISIBLE
                    binding.btResetPassword.isClickable = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = INVISIBLE
                    binding.tvEmail.visibility = VISIBLE
                    binding.etEmail.visibility = VISIBLE
                    binding.btResetPassword.isClickable = true
                    viewModel.showToastMessage(PASSWORD_RESET_MESSAGE)
                    viewModel.onNavigationToSignInStart()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = INVISIBLE
                    binding.tvEmail.visibility = VISIBLE
                    binding.etEmail.visibility = VISIBLE
                    binding.btResetPassword.isClickable = true
                    viewModel.showToastMessage(it.message ?: UNKNOWN_ERROR_MESSAGE)
                }
            }
        }
    }

    private fun observeErrorMessageState() {
        // email error
        viewModel.emailError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.etEmail.requestFocus()
                binding.etEmail.error = errorMessage
                delay(ERROR_MESSAGE_DELAY) { binding.etEmail.clearFocus() }
            }
        }
    }

    private fun observeToastMessageState() {
        // toast message visibility
        viewModel.toastMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeNavigationToSignInState() {
        // navigate to sign in activity
        viewModel.navigateToSignIn.observe(this) { navigateToSignIn ->
            if (navigateToSignIn) {
                navigateToSignInActivity()
                viewModel.onNavigationToSignInComplete()
            }
        }
    }

    private fun navigateToSignInActivity() = finish()

    private fun onBackClicked() {
        // back button click listener
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun onResetPasswordClicked() {
        // reset password button click listener
        binding.btResetPassword.setOnClickListener {
            closeKeyboard(this, it)

            // get email
            val email = binding.etEmail.text.toString().trim().lowercase()
            // validate email
            if (viewModel.validateUserResetPasswordDetails(email)) {
                // reset password
                viewModel.resetPassword(email)
            }
        }
    }
}