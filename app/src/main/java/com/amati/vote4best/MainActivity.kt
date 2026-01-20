package com.amati.vote4best

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amati.vote4best.data.model.AuthState
import com.amati.vote4best.ui.screens.HomeScreen
import com.amati.vote4best.ui.screens.LoginScreen
import com.amati.vote4best.ui.theme.Vote4BestTheme
import com.amati.vote4best.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authViewModel = AuthViewModel()
        setContent {
            val authState by authViewModel.authState.collectAsState()

            when(authState){
                is AuthState.Authenticated ->{
                    HomeScreen(authViewModel)
                }
                is AuthState.Guest ->{
                    LoginScreen(authViewModel)
                }
                else -> {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}