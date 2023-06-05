package com.dolla.mrimate.viewModel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolla.mrimate.pojo.IOState
import com.dolla.mrimate.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class RegistrationViewModel : ViewModel() {

    private val coroutineScope =
        CoroutineScope(Dispatchers.IO) // create a coroutine scope that runs on the IO dispatcher

    // ############################## View Utils ##############################
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?>
        get() = _toastMessage

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

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


    private val _navigateToHomeActivity = MutableLiveData<Boolean>()
    val navigateToHomeActivity: LiveData<Boolean>
        get() = _navigateToHomeActivity

    // live data for the sign up result
    private val _userRegistrationStatus = MutableLiveData<IOState?>()
    val userRegistrationStatus: MutableLiveData<IOState?>
        get() = _userRegistrationStatus

    // live data for the sign up result
    private val _userSignInStatus = MutableLiveData<IOState?>()
    val userSignInStatus: MutableLiveData<IOState?>
        get() = _userSignInStatus


    // ############################## Authentication ##############################
//    private val _loginResponseLiveData =
//        MutableLiveData<LoginResponse?>() // initialize the loginResponse live data
//    val loginResponseLiveData: LiveData<LoginResponse?>
//        get() = _loginResponseLiveData // This is a read-only property that returns the value of the private property _loginResponseLiveData

    init {
        _isLoading.value = false // set the loading state to false by default
        _navigateToHomeActivity.value = false // set the navigation state to false by default
    }

    private fun onLoadingStarted() {
        _isLoading.value = true
    }

    private fun onLoadingFinished() {
        _isLoading.value = false
    }

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }

    fun onToastMessageShown() {
        _toastMessage.value = null // set the toast message to null after it has been shown
    }


    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun onErrorMessageShown() {
        _errorMessage.value = null // set the error message to null after it has been shown
    }

    fun onNavigateToHomeActivityStart() {
        _navigateToHomeActivity.value = true
    }

    fun onNavigationToHomeActivityFinished() {
        _navigateToHomeActivity.value = false
    }

    fun setUserRegistrationStatus(status: IOState) {
        _userRegistrationStatus.value = status
    }

    fun onUserRegistrationStatusShown() {
        _userRegistrationStatus.value = null
    }

    fun setUserSignInStatus(status: IOState) {
        _userSignInStatus.value = status
    }

    fun onUserSignInStatusShown() {
        _userSignInStatus.value = null
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
            setToastMessage(INVALID_PASSWORD_MESSAGE)
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

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel() // cancel the coroutine scope when the view model is cleared
    }
}