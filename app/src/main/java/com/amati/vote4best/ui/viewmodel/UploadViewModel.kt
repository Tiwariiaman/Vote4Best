package com.amati.vote4best.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.amati.vote4best.data.model.Place
import com.amati.vote4best.data.model.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UploadViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun uploadPlace(
        imageUri: Uri,
        city: String,
        latitude: Double,
        longitude: Double,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return

        val fileRef = storage.reference
            .child("places/${System.currentTimeMillis()}.jpg")

        fileRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("Upload failed")
                }
                fileRef.downloadUrl
            }
            .addOnSuccessListener { downloadUrl ->
                val place = Places(
                    imageUrl = downloadUrl.toString(),
                    city = city,
                    latitude = latitude,
                    longitude = longitude,
                    uploadedBy = userId
                )

                firestore.collection("places")
                    .add(place)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onError(it.message ?: "Firestore error")
                    }
            }
            .addOnFailureListener {
                onError(it.message ?: "Storage error")
            }
    }
}
