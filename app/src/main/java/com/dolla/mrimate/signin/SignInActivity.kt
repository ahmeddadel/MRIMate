package com.dolla.mrimate.signin

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dolla.mrimate.databinding.ActivitySignInBinding
import com.dolla.mrimate.resetpassword.ResetPasswordActivity
import com.dolla.mrimate.signup.SignUpActivity
import com.dolla.mrimate.ui.activities.HostActivity
import com.dolla.mrimate.ui.activities.USER_IS_DOCTOR_KEY
import com.dolla.mrimate.util.*

const val EMAIL_INTENT_KEY = "com.dolla.mrimate.EMAIL_INTENT_KEY"

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentViewCustom(binding.root)

        // set the view model
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        // set the email edit text text
        setEmailEditText()
        // observe progress bar and sign in layout visibility
        observeSigningInState()
        // error message visibility
        observeErrorMessageState()
        // toast message visibility
        observeToastMessageState()
        // observe the sign up navigation state
        observeNavigationToSignUpState()
        // observe the reset password navigation state
        observeNavigationToResetPasswordState()
        // observe the home navigation state
        observeNavigationToHomeState()
        // create account text view click listener
        onCreateAccountClicked()
        // forget password text view click listener
        onResetPasswordClicked()
        // sign in button click listener
        onSignInClicked()
    }

    private fun setEmailEditText() {
        // set the email edit text text
        binding.etEmail.setText(intent.getStringExtra(EMAIL_INTENT_KEY) ?: "")
    }

    private fun observeSigningInState() {
        // sign in views visibility when loading
        viewModel.userSignInStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = VISIBLE
                    binding.layoutSignIn.visibility = INVISIBLE
                    binding.btSignIn.isClickable = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = INVISIBLE
                    binding.layoutSignIn.visibility = VISIBLE
                    binding.btSignIn.isClickable = true
                    viewModel.showToastMessage(SIGN_IN_SUCCESS_MESSAGE)
                    viewModel.onNavigationToHomeStart()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = INVISIBLE
                    binding.layoutSignIn.visibility = VISIBLE
                    binding.btSignIn.isClickable = true
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
        // password error
        viewModel.passwordError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.etPassword.requestFocus()
                binding.etPassword.error = errorMessage
                delay(ERROR_MESSAGE_DELAY) { binding.etPassword.clearFocus() }
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

    private fun observeNavigationToSignUpState() {
        // navigate to sign in activity
        viewModel.navigateToSignUp.observe(this) { navigateToSignUp ->
            if (navigateToSignUp) {
                navigateToSignUpActivity()
                viewModel.onNavigationToSignUpComplete()
            }
        }
    }

    private fun observeNavigationToResetPasswordState() {
        // navigate to reset password activity
        viewModel.navigateToResetPassword.observe(this) { navigateToResetPassword ->
            if (navigateToResetPassword) {
                navigateToResetPasswordActivity()
                viewModel.onNavigationToResetPasswordComplete()
            }
        }
    }

    private fun observeNavigationToHomeState() {
        // navigate to home activity
        viewModel.navigateToHome.observe(this) { navigateToHome ->
            if (navigateToHome) {
                navigateToHomeActivity()
                viewModel.onNavigationToHomeComplete()
            }
        }
    }

    private fun onCreateAccountClicked() {
        // create account text view click listener
        binding.tvCreateAccount.setOnClickListener {
            closeKeyboard(this, it)
            viewModel.onNavigationToSignUpStart()
        }
    }

    private fun onResetPasswordClicked() {
        // forget password text view click listener
        binding.tvForgetPassword.setOnClickListener {
            closeKeyboard(this, it)
            viewModel.onNavigationToResetPasswordStart()
        }
    }

    private fun navigateToSignUpActivity() {
        // navigate to sign up activity
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun navigateToResetPasswordActivity() {
        // navigate to reset password activity
        startActivity(Intent(this, ResetPasswordActivity::class.java))
    }

    private fun navigateToHomeActivity() {
        // navigate to home activity
        val intent = Intent(this, HostActivity::class.java)
        intent.putExtra(USER_IS_DOCTOR_KEY, viewModel.userIsDoctor)
        startActivity(intent)
        finish()
    }

    private fun onSignInClicked() {
        // sign in button click listener
        binding.btSignIn.setOnClickListener {
            closeKeyboard(this, it)

            // get the email and password from the edit text
            val email = binding.etEmail.text.toString().trim().lowercase()
            val password = binding.etPassword.text.toString()

            // check if the user details are valid
            if (viewModel.validateUserSignInDetails(email, password)) {
                // sign in the user
                viewModel.signIn(email, password)
            }
        }
    }
}