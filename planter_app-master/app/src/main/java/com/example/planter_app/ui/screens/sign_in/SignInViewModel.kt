package com.example.planter_app.ui.screens.sign_in

import androidx.lifecycle.ViewModel
import com.example.planter_app.firebase_login.sign_in.SignInResult
import com.example.planter_app.firebase_login.sign_in.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class SignInViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _loadingIcon = MutableStateFlow(false)
    val loadingIcon: StateFlow<Boolean> = _loadingIcon.asStateFlow()

    fun setLoadingIcon(loading: Boolean) {
        _loadingIcon.value = loading
    }

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        // setting it back to default values
        _state.update { SignInState() }
    }

}