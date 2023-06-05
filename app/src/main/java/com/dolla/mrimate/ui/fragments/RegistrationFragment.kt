package com.dolla.mrimate.ui.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dolla.mrimate.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding // View binding
    private var applicationContext: Context? = null// Application context

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        applicationContext =
            requireActivity().applicationContext // get application context from the activity that is attached to the fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view

        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        onSignInClicked() // Set the onClickListener for the sign in button
        onCreateAccountClicked() // Set the onClickListener for the sign up button
    }

    private fun onSignInClicked() { // Set the onClickListener for sign in button
        binding.btSignIn.setOnClickListener {
            findNavController().navigate(
                RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment()
            ) // Navigate to the sign in fragment
        }
    }

    private fun onCreateAccountClicked() { // Set the onClickListener for the sign up button
        binding.btCreateAccount.setOnClickListener {
            findNavController().navigate(
                RegistrationFragmentDirections.actionRegistrationFragmentToSignUpFragment()
            ) // Navigate to the sign up fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext = null // Clear the application context to avoid memory leaks
    }
}