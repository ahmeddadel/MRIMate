package com.dolla.mrimate.ui.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dolla.mrimate.R
import com.dolla.mrimate.databinding.FragmentSignUpBinding
import com.dolla.mrimate.pojo.Doctor
import com.dolla.mrimate.pojo.IOState
import com.dolla.mrimate.pojo.Patient
import com.dolla.mrimate.util.appendToFile
import com.dolla.mrimate.util.hideKeyboard
import com.dolla.mrimate.viewModel.RegistrationViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding // View binding
    private lateinit var viewModel: RegistrationViewModel // View model
    private var applicationContext: Context? = null// Application context

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        applicationContext =
            requireActivity().applicationContext // get application context from the activity that is attached to the fragment
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view

        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        observeRadioButtonState() // observe radio button for patient or doctor
        observeSigningUpState() // observe the signing up state
        observeErrorMessage() // observe the error message state
        observeToastMessage() // observe the toast message state
        onSignUpClicked() // Set the onClickListener for the sign up button
        onSignInClicked() // Set the onClickListener for the sign in button
    }

    private fun observeRadioButtonState() {
        // set the radio button checked change listener
        binding.rbPatient.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tvHospitalId.visibility = View.GONE
                binding.etHospitalId.visibility = View.GONE
            } else {
                binding.tvHospitalId.visibility = View.VISIBLE
                binding.etHospitalId.visibility = View.VISIBLE
            }
        }
    }

    private fun observeSigningUpState() {
        viewModel.userRegistrationStatus.observe(viewLifecycleOwner) { // observe the user registration status
            when (it) {
                IOState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutSignUp.visibility = View.INVISIBLE
                    binding.radioGroup.visibility = View.INVISIBLE
                    binding.btCreateAccount.isClickable = false
                }
                IOState.SUCCESS -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.layoutSignUp.visibility = View.VISIBLE
                    binding.radioGroup.visibility = View.VISIBLE
                    binding.btCreateAccount.isClickable = true
                    viewModel.setToastMessage("Sign up successful")
                }
                else -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.layoutSignUp.visibility = View.VISIBLE
                    binding.radioGroup.visibility = View.VISIBLE
                    binding.btCreateAccount.isClickable = true
                }
            }
        }
    }

    private fun onSignUpClicked() {
        // sign up button click listener
        binding.btCreateAccount.setOnClickListener {
            requireActivity().hideKeyboard() // hide the keyboard

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
                signUp(email, name, phone, password, hospitalId)
            }
        }
    }

    private fun signUp(
        email: String,
        name: String,
        phone: String,
        password: String,
        hospitalId: String
    ) {
        viewModel.setUserRegistrationStatus(IOState.LOADING)

        lifecycleScope.launch(Dispatchers.IO) {

            delay(2000) // delay for 2 seconds

            if (binding.rbDoctor.isChecked) {
                val user = Doctor(email, name, phone, password, hospitalId)
                user.appendToFile(applicationContext!!)
            } else {
                val user = Patient(email, name, phone, password)
                user.appendToFile(applicationContext!!)
            }

            withContext(Dispatchers.Main) {
                viewModel.setUserRegistrationStatus(IOState.SUCCESS)
                findNavController().navigate(
                    SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(
                        email
                    )
                )
                viewModel.onUserRegistrationStatusShown()
            }
        }
    }

    private fun observeErrorMessage() {
        // email error
        viewModel.emailError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                binding.etEmail.requestFocus()
                binding.etEmail.error = errorMessage
            }
        }
        // name error
        viewModel.nameError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                binding.etName.requestFocus()
                binding.etName.error = errorMessage
            }
        }
        // phone error
        viewModel.phoneError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                binding.etPhone.requestFocus()
                binding.etPhone.error = errorMessage
            }
        }
        // password error
        viewModel.passwordError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                binding.etPassword.requestFocus()
                binding.etPassword.error = errorMessage
            }
        }
        // hospital id error
        viewModel.hospitalIdError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                binding.etHospitalId.requestFocus()
                binding.etHospitalId.error = errorMessage
            }
        }
        viewModel.onErrorMessageShown()
    }

    private fun observeToastMessage() {
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toasty.custom(
                    applicationContext!!,
                    message,
                    R.drawable.ic_logo,
                    R.color.colorPrimaryVariant,
                    Toast.LENGTH_LONG,
                    true,
                    true
                ).show()
                viewModel.onToastMessageShown()
            }
        }
    }

    private fun onSignInClicked() {
        binding.layoutSignIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext = null // Clear the application context to avoid memory leaks
    }
}