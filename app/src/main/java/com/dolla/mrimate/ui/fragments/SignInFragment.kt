package com.dolla.mrimate.ui.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dolla.mrimate.R
import com.dolla.mrimate.databinding.FragmentSignInBinding
import com.dolla.mrimate.pojo.IOState
import com.dolla.mrimate.ui.activities.HomeActivity
import com.dolla.mrimate.util.*
import com.dolla.mrimate.viewModel.RegistrationViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding // View binding
    private lateinit var viewModel: RegistrationViewModel // View model
    private var applicationContext: Context? = null// Application context
    private var email: String? = "" // email

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
        binding = FragmentSignInBinding.inflate(inflater)

        email = arguments?.getString("email")

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        setEmailEditText() // set the email edit text

        observeSigningInState() // observe the signing in state
        observeErrorMessageState() // observe the error message state
        observeToastMessage() // observe the toast message state

        onCreateAccountClicked() // create account text view click listener
        onSignInClicked() // sign in button click listener
        //onResetPasswordClicked() // reset password text view click listener
    }

    private fun setEmailEditText() {
        binding.etEmail.setText(email)
    }

    private fun observeSigningInState() {
        viewModel.userSignInStatus.observe(viewLifecycleOwner) { // observe the sign in status
            when (it) {
                IOState.LOADING -> {
                    binding.progressBar.visibility = VISIBLE
                    binding.layoutSignIn.visibility = INVISIBLE
                    binding.btSignIn.isClickable = false
                }
                IOState.SUCCESS -> {
                    binding.progressBar.visibility = INVISIBLE
                    binding.layoutSignIn.visibility = VISIBLE
                    binding.btSignIn.isClickable = true
                    viewModel.setToastMessage("Sign in successful")
                }
                else -> {
                    binding.progressBar.visibility = INVISIBLE
                    binding.layoutSignIn.visibility = VISIBLE
                    binding.btSignIn.isClickable = true
                }
            }
        }
    }

    private fun observeErrorMessageState() {
        // email error
        viewModel.emailError.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                binding.etEmail.requestFocus()
                binding.etEmail.error = errorMessage
            }
        }
        // password error
        viewModel.passwordError.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                binding.etPassword.requestFocus()
                binding.etPassword.error = errorMessage
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

    private fun onCreateAccountClicked() {
        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }

    private fun onSignInClicked() {
        // sign in button click listener
        binding.btSignIn.setOnClickListener {
            requireActivity().hideKeyboard() // hide the keyboard

            // get the email and password from the edit text
            val email = binding.etEmail.text.toString().trim().lowercase()
            val password = binding.etPassword.text.toString()

            // check if the user details are valid
            if (viewModel.validateUserSignInDetails(email, password)) {
                // sign in the user
                signIn(email, password)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModel.setUserSignInStatus(IOState.LOADING)

        lifecycleScope.launch(Dispatchers.IO) {

            delay(2000) // delay for 2 seconds

            val patients = readPatientsFromFile(applicationContext!!)
            val doctors = readDoctorsFromFile(applicationContext!!)

            for (patient in patients) {
                if (patient.email == email && patient.password == password) {
                    withContext(Dispatchers.Main) {
                        viewModel.setUserSignInStatus(IOState.SUCCESS)
                        isDoctor = false
                        patientObj = patient

                        navigateToHomeActivity()
                        viewModel.onUserSignInStatusShown()
                    }
                    return@launch
                }
            }

            for (doctor in doctors) {
                if (doctor.email == email && doctor.password == password) {
                    withContext(Dispatchers.Main) {
                        viewModel.setUserSignInStatus(IOState.SUCCESS)
                        isDoctor = true
                        doctorObj = doctor

                        navigateToHomeActivity()
                        viewModel.onUserSignInStatusShown()
                    }
                    return@launch
                }
            }

            withContext(Dispatchers.Main) {
                viewModel.setUserSignInStatus(IOState.ERROR)
                viewModel.setToastMessage("Invalid email or password")
            }
        }
    }

    private fun navigateToHomeActivity() {
        startActivity(Intent(requireActivity(), HomeActivity::class.java))
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext = null // Clear the application context to avoid memory leaks
    }
}