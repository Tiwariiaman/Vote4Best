package com.amati.vote4best.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amati.vote4best.ui.components.BottomNavItem
import com.amati.vote4best.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(authViewModel: AuthViewModel) {

    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Upload, BottomNavItem.Places
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Vote4Best") }, actions = {
                TextButton(onClick = {
                    authViewModel.logout()
                }) {
                    Text("Logout")
                }
            })
        },
            bottomBar = {
                NavigationBar() {
                    val currentRoute =
                        navController.currentBackStackEntryAsState().value
                            ?.destination?.route

                    items.forEach {
                        item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route){
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop =true
                                }
                            },
                            icon = {
                                Icon(item.icon, contentDescription = item.title)
                            },
                            label = {
                                Text(item.title)
                            }
                        )
                    }
                }
            }

        ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Places.route,
            modifier = Modifier.padding(paddingValues)
        ){
            composable(BottomNavItem.Upload.route){
                UploadScreen(authViewModel)
            }
            composable(BottomNavItem.Places.route){
                PlacesScreen(authViewModel)
            }
        }
    }

}