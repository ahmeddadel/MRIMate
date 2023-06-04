package com.dolla.mrimate.resetpassword

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolla.mrimate.repo.FirebaseAuthManager
import com.dolla.mrimate.util.EMPTY_EMAIL_ERROR
import com.dolla.mrimate.util.INVALID_EMAIL_ERROR
import com.dolla.mrimate.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @created 23/01/2023 - 7:12 AM
 * @project MRIMate
 * @author adell
 */
class ResetPasswordViewModel : ViewModel() {
    // live data for the error message to be displayed to the user when the user details are invalid
    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    // live data for the toast message to be displayed
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    // live data for the navigation to the sign in activity
    private val _navigateToSignIn = MutableLiveData<Boolean>()
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn

    // live data for the reset password result
    private val _userResetPasswordStatus = MutableLiveData<Resource<Unit>>()
    val userResetPasswordStatus: LiveData<Resource<Unit>>
        get() = _userResetPasswordStatus

    init {
        _navigateToSignIn.value = false
    }

    fun onNavigationToSignInStart() {
        _navigateToSignIn.value = true
    }

    fun onNavigationToSignInComplete() {
        _navigateToSignIn.value = false
    }

    // validate the user sign in details
    fun validateUserResetPasswordDetails(email: String): Boolean {
        if (email.isEmpty()) {
            _emailError.value = EMPTY_EMAIL_ERROR
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = INVALID_EMAIL_ERROR
            return false
        }
        return true
    }

    fun showToastMessage(message: String) {
        _toastMessage.value = message
    }

    fun resetPassword(email: String) {
        _userResetPasswordStatus.value = Resource.Loading()

        viewModelScope.launch(Dispatchers.Main) {
            val resetResult = FirebaseAuthManager.resetPassword(email)
            _userResetPasswordStatus.postValue(resetResult)
        }
    }
}