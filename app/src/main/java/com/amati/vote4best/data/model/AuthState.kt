package com.amati.vote4best.data.model

import android.os.Message

sealed class AuthState {
    object Loading: AuthState()
    object Authenticated: AuthState()
    object Guest: AuthState()
    data class Error(val message: String): AuthState()
}
