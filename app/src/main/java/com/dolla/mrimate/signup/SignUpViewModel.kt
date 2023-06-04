package com.dolla.mrimate.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolla.mrimate.repo.FirebaseAuthManager
import com.dolla.mrimate.util.*
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @created 29/12/2022 - 11:59 PM
 * @project MRIMate
 * @author adell
 */
class SignUpViewModel : ViewModel() {

    // live data for the error message to be displayed to the user when the sign up fails or when the user details are invalid
    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private val _nameError = MutableLiveData<String>()
    val nameError: LiveData<String>
        get() = _nameError

    private val _phoneError = MutableLiveData<String>()
    val phoneError: LiveData<String>
        get() = _phoneError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError

    private val _hospitalIdError = MutableLiveData<String>()
    val hospitalIdError: LiveData<String>
        get() = _hospitalIdError

    // live data for the toast message to be displayed
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    // live data for the navigation to the sign in activity
    private val _navigateToSignIn = MutableLiveData<Boolean>()
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn

    // live data for the sign up result
    private val _userRegistrationStatus = MutableLiveData<Resource<AuthResult>>()
    val userRegistrationStatus: LiveData<Resource<AuthResult>>
        get() = _userRegistrationStatus

    init {
        _navigateToSignIn.value = false
    }

    fun onNavigationToSignInStart() {
        _navigateToSignIn.value = true
    }

    fun onNavigationToSignInCompleted() {
        _navigateToSignIn.value = false
    }

    // validate the user sign up details
    fun validateUserSignUpDetails(
        email: String,
        name: String,
        phone: String,
        password: String,
        hospitalId: String,
        isDoctor: Boolean
    ): Boolean {
        if (email.isEmpty()) {
            _emailError.value = EMPTY_EMAIL_ERROR
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = INVALID_EMAIL_ERROR
            return false
        }
        if (name.isEmpty()) {
            _nameError.value = EMPTY_NAME_ERROR
            return false
        }
        if (phone.isEmpty()) {
            _phoneError.value = EMPTY_PHONE_NUMBER_ERROR
            return false
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            _phoneError.value = INVALID_PHONE_NUMBER_ERROR
            return false
        }
        if (password.isEmpty()) {
            _passwordError.value = EMPTY_PASSWORD_ERROR
            return false
        }
        if (password.length < 6) {
            _passwordError.value = PASSWORD_LENGTH_ERROR
            return false
        }
        if (!PASSWORD_REGEX.matches(password)) {
            _passwordError.value = INVALID_PASSWORD_ERROR
            showToastMessage(INVALID_PASSWORD_MESSAGE)
            return false
        }
        if (isDoctor) {
            if (hospitalId.isEmpty()) {
                _hospitalIdError.value = EMPTY_HOSPITAL_ID_ERROR
                return false
            }
        }
        return true
    }

    fun showToastMessage(message: String) {
        _toastMessage.value = message
    }

    fun signUp(
        email: String,
        name: String,
        phone: String,
        password: String,
        isDoctor: Boolean,
        hospitalId: String
    ) {
        _userRegistrationStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val registrationResult =
                FirebaseAuthManager.createUser(email, name, phone, password, isDoctor, hospitalId)
            _userRegistrationStatus.postValue(registrationResult)
        }
    }
}