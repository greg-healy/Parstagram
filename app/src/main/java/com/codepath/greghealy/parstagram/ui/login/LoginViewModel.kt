package com.codepath.greghealy.parstagram.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.codepath.greghealy.parstagram.data.LoginRepository
import com.codepath.greghealy.parstagram.data.Result

import com.codepath.greghealy.parstagram.R
import com.parse.LogInCallback
import com.parse.ParseUser
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    val TAG = this.javaClass.simpleName

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            ParseUser.logInInBackground(username, password, LogInCallback { user, e ->
                if (e != null) {
                    Log.e(TAG, "Issue with login", e)
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                } else {
                    Log.i(TAG, "Login success! $user")
                    _loginResult.value = LoginResult(success = LoggedInUserView(username))
                }
            })
//            val result = loginRepository.login(username, password)
//
//            if (result is Result.Success) {
//                _loginResult.value =
//                    LoginResult(success = LoggedInUserView(displayName = result.data))
//            } else {
//                _loginResult.value = LoginResult(error = R.string.login_failed)
//            }
        }
    }

    fun signup(username: String, password: String) {
        viewModelScope.launch {
            val user = ParseUser()

            with(user) {
                this.username = username
                setPassword(password)
                signUpInBackground {
                    if (it != null) {
                        Log.e(TAG, "Signup failed", it)
                    } else {
                        login(username, password)
                    }
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 1
    }
}