package com.amati.vote4best.data.model

data class Place(
    val imageUrl: String = "",
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val voteCount: Int = 0,
    val uploadedBy: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
