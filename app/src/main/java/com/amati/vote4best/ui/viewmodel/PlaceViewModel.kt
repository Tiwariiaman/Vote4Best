package com.amati.vote4best.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amati.vote4best.data.model.Place
import com.amati.vote4best.data.repository.PlacesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PlacesViewModel : ViewModel() {

    private val repository = PlacesRepository()

    val places = repository.getPlaces()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun vote(place: Place) {
        repository.vote(place)
    }
}