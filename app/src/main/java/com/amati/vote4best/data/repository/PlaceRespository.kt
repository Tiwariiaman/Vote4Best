package com.amati.vote4best.data.repository

import com.amati.vote4best.data.model.Place
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class PlacesRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getPlaces() = callbackFlow<List<Place>> {
        val listener = firestore.collection("places")
            .orderBy("voteCount", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val places = snapshot?.documents?.map { doc ->
                    Place(
                        id = doc.id,
                        imageUrl = doc.getString("imageUrl") ?: "",
                        city = doc.getString("city") ?: "",
                        latitude = doc.getDouble("latitude") ?: 0.0,
                        longitude = doc.getDouble("longitude") ?: 0.0,
                        voteCount = (doc.getLong("voteCount") ?: 0L).toInt(),
                        votedBy = doc.get("votedBy") as? Map<String, Any> ?: emptyMap(),
                        uploadedBy = doc.getString("uploadedBy") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                } ?: emptyList()

                trySend(places)
            }

        awaitClose { listener.remove() }
    }

    fun vote(place: Place) {
        val userId = auth.currentUser?.uid ?: return
        val ref = firestore.collection("places").document(place.id)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(ref)
            val votedBy =
                snapshot.get("votedBy") as? MutableMap<String, Boolean> ?: mutableMapOf()

            if (votedBy.containsKey(userId)) {
                return@runTransaction
            }

            votedBy[userId] = true
            val currentVotes = snapshot.getLong("voteCount") ?: 0

            transaction.update(ref, mapOf(
                "voteCount" to currentVotes + 1,
                "votedBy" to votedBy
            ))
        }
    }
}