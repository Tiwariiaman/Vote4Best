package com.amati.vote4best.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.amati.vote4best.data.model.AuthState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel: ViewModel() {

    private  val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState


    init {
        checkAuthState()
    }

    private fun checkAuthState(){
        _authState.value =
            if(auth.currentUser !=null){
                AuthState.Authenticated
            }else{
                AuthState.Guest
            }
    }

    fun login(email: String, password: String){
        if(email.isBlank() || password.isBlank()){
            _authState.value = AuthState.Error("Email or Password can't be Empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Authenticated
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Login Failed")

            }
    }

    fun logout(){
        auth.signOut()
        _authState.value = AuthState.Guest
    }
}