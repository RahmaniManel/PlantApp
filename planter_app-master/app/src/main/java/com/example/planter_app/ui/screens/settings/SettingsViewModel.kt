package com.example.planter_app.ui.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planter_app.utilities.ConnectivityCheck
import com.example.planter_app.MyApplication
import com.example.planter_app.firebase_login.sign_in.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel:ViewModel(){

    companion object{
        var appBarTitle = mutableStateOf("")

        val darkMode = mutableStateOf(false)
        val dynamicTheme = mutableStateOf(false)

        val isNetworkAvailable = mutableStateOf(true)
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()


    fun updateConnectionStatus(){
    isNetworkAvailable.value = ConnectivityCheck.checkNetworkAvailability()
    }

    fun updateRefresh() {
        _isRefreshing.value = true
        updateConnectionStatus()

        viewModelScope.launch {
            delay(1000L)
            _isRefreshing.value = false
        }
    }

    fun isUserLoggedIn(): Boolean{
        val googleAuthUiClient by lazy {
            GoogleAuthUiClient(
                oneTapClient = Identity.getSignInClient(MyApplication.instance!!.applicationContext)
            )
        }
        return googleAuthUiClient.getSignedInUser() != null
    }


}