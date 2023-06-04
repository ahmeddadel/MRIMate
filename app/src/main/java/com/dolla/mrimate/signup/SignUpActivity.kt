package com.dolla.mrimate.signup

import android.content.Intent
import android.os.Bundle
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dolla.mrimate.databinding.ActivitySignUpBinding
import com.dolla.mrimate.signin.EMAIL_INTENT_KEY
import com.dolla.mrimate.signin.SignInActivity
import com.dolla.mrimate.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentViewCustom(binding.root)

        // initialize the view model
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        // observe radio button for patient or doctor
        observeRadioButtonState()
        // observe progress bar and sign up layout visibility
        observeSigningUpState()
        // error message visibility
        observeErrorMessageState()
        // toast message visibility
        observeToastMessageState()
        // observe the sign in navigation state
        observeNavigationToSignInState()
        // sign in text view click listener
        onSignInClicked()
        // sign up button click listener
        onSignUpClicked()
    }

    private fun observeRadioButtonState() {
        // set the radio button checked change listener
        binding.rbPatient.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tvHospitalId.visibility = GONE
                binding.etHospitalId.visibility = GONE
            } else {
                binding.tvHospitalId.visibility = VISIBLE
                binding.etHospitalId.visibility = VISIBLE
            }
        }
    }

    private fun observeSigningUpState() {
        // sign up views visibility when loading
        viewModel.userRegistrationStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = VISIBLE
                    binding.layoutSignUp.visibility = INVISIBLE
                    binding.radioGroup.visibility = INVISIBLE
                    binding.btCreateAccount.isClickable = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = INVISIBLE
                    binding.layoutSignUp.visibility = VISIBLE
                    binding.radioGroup.visibility = VISIBLE
                    binding.btCreateAccount.isClickable = true
                    viewModel.showToastMessage(VERIFICATION_EMAIL_SENT_MESSAGE)
                    viewModel.onNavigationToSignInStart()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = INVISIBLE
                    binding.layoutSignUp.visibility = VISIBLE
                    binding.radioGroup.visibility = VISIBLE
                    binding.btCreateAccount.isClickable = true
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
        // name error
        viewModel.nameError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.etName.requestFocus()
                binding.etName.error = errorMessage
                delay(ERROR_MESSAGE_DELAY) { binding.etName.clearFocus() }
            }
        }
        // phone error
        viewModel.phoneError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.etPhone.requestFocus()
                binding.etPhone.error = errorMessage
                delay(ERROR_MESSAGE_DELAY) { binding.etPhone.clearFocus() }
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
        // hospital id error
        viewModel.hospitalIdError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.etHospitalId.requestFocus()
                binding.etHospitalId.error = errorMessage
                delay(ERROR_MESSAGE_DELAY) { binding.etHospitalId.clearFocus() }
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
                viewModel.onNavigationToSignInCompleted()
            }
        }
    }

    private fun onSignInClicked() {
        // sign in text view click listener
        binding.tvSignIn.setOnClickListener {
            closeKeyboard(this, it)
            viewModel.onNavigationToSignInStart()
        }
    }

    private fun navigateToSignInActivity() {
        // navigate to sign in activity
        val intent = Intent(this, SignInActivity::class.java)
        intent.putExtra(EMAIL_INTENT_KEY, binding.etEmail.text.toString().trim().lowercase())
        startActivity(intent)
        finish()
    }

    private fun onSignUpClicked() {
        // sign up button click listener
        binding.btCreateAccount.setOnClickListener {
            closeKeyboard(this, it)

            // get the user details from the edit text fields
            val email = binding.etEmail.text.toString().trim().lowercase()
            val name = binding.etName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val hospitalId = binding.etHospitalId.text.toString().trim()

            // check if the user details are valid
            if (viewModel.validateUserSignUpDetails(
                    email,
                    name,
                    phone,
                    password,
                    hospitalId,
                    binding.rbDoctor.isChecked
                )
            ) {
                viewModel.signUp(
                    email,
                    name,
                    phone,
                    password,
                    binding.rbDoctor.isChecked,
                    hospitalId
                )
            }
        }
    }
}