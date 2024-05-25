package com.example.planter_app

import android.app.Application
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.planter_app.firebase_login.sign_in.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: MyApplication? = null
            private set
    }
}
