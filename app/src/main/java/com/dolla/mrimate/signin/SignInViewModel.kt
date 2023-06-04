package com.dolla.mrimate.signin

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolla.mrimate.repo.FirebaseAuthManager
import com.dolla.mrimate.repo.FirebaseCloudManager
import com.dolla.mrimate.util.*
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @created 29/12/2022 - 10:13 PM
 * @project MRIMate
 * @author adell
 */
class SignInViewModel : ViewModel() {

    // live data for the error message to be displayed to the user when the sign in fails or when the user details are invalid
    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError

    // live data for the toast message to be displayed
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    // live data for the navigation to the sign up activity
    private val _navigateToSignUp = MutableLiveData<Boolean>()
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    // live data for the navigation to the reset password activity
    private val _navigateToResetPassword = MutableLiveData<Boolean>()
    val navigateToResetPassword: LiveData<Boolean>
        get() = _navigateToResetPassword

    // live data for the navigation to the home activity
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    // live data for the sign in result
    private val _userSignInStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignInStatus: LiveData<Resource<AuthResult>>
        get() = _userSignInStatus

    var userIsDoctor: Boolean? = false

    init {
        _navigateToSignUp.value = false
        _navigateToResetPassword.value = false
        _navigateToHome.value = false
    }

    fun onNavigationToSignUpStart() {
        _navigateToSignUp.value = true
    }

    fun onNavigationToSignUpComplete() {
        _navigateToSignUp.value = false
    }

    fun onNavigationToResetPasswordStart() {
        _navigateToResetPassword.value = true
    }

    fun onNavigationToResetPasswordComplete() {
        _navigateToResetPassword.value = false
    }

    fun onNavigationToHomeStart() {
        _navigateToHome.value = true
    }

    fun onNavigationToHomeComplete() {
        _navigateToHome.value = false
    }

    // validate the user sign in details
    fun validateUserSignInDetails(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            _emailError.value = EMPTY_EMAIL_ERROR
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = INVALID_EMAIL_ERROR
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
        return true
    }

    fun showToastMessage(message: String) {
        _toastMessage.value = message
    }

    fun signIn(email: String, password: String) {
        _userSignInStatus.value = Resource.Loading()

        viewModelScope.launch(Dispatchers.Main) {
            val loggingResult: Resource<AuthResult> = FirebaseAuthManager.signIn(email, password)
            userIsDoctor =
                FirebaseCloudManager.getUserTypeFromFirestore(loggingResult.data?.user?.uid.toString())
            _userSignInStatus.postValue(loggingResult)
        }
    }
}