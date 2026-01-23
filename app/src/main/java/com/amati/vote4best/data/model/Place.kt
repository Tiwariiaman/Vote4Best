package com.amati.vote4best.data.model

data class Place(
    val id: String = "",
    val imageUrl: String = "",
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val voteCount: Int = 0,
    val votedBy: Map<String, Any> = emptyMap(),
    val uploadedBy: String = "",
    val timestamp: Long = 0L
)