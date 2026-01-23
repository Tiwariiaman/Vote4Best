package com.amati.vote4best.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amati.vote4best.data.model.Place
import com.amati.vote4best.ui.components.PlaceCard
import com.amati.vote4best.ui.viewmodel.AuthViewModel
import com.amati.vote4best.ui.viewmodel.PlacesViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun PlacesScreen(
    authViewModel: AuthViewModel
) {
    val viewModel = remember { PlacesViewModel() }
    val places by viewModel.places.collectAsState()

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(places) { place ->
            val canVote =
                userId != null && !place.votedBy.containsKey(userId)

            PlaceCard(
                place = place,
                canVote = canVote,
                onVoteClick = {
                    if (userId == null) return@PlaceCard
                    viewModel.vote(place)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}