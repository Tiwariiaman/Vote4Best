package com.amati.vote4best.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector){

    object Upload: BottomNavItem("upload", "Upload", Icons.Default.Add)

    object Places: BottomNavItem("places", "Places", Icons.Default.Home)

}