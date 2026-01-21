package com.amati.vote4best.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import coil.compose.AsyncImage
import com.amati.vote4best.ui.utlis.getCityName
import com.amati.vote4best.ui.utlis.getCurrentLocation
import com.amati.vote4best.ui.utlis.saveBitmapToCache
import com.amati.vote4best.ui.viewmodel.AuthViewModel
import com.amati.vote4best.ui.viewmodel.UploadViewModel


@Composable
fun UploadScreen(
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val uploadViewModel = remember { UploadViewModel() }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var pendingUpload by remember { mutableStateOf(false) }

    /* ---------------- CAMERA LAUNCHER ---------------- */

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap: Bitmap? ->
            bitmap?.let {
                imageUri = saveBitmapToCache(context, it)
            }
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                cameraLauncher.launch(null)
            } else {
                Toast.makeText(
                    context,
                    "Camera permission is required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    /* ---------------- LOCATION PERMISSION ---------------- */

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted && pendingUpload && imageUri != null) {
                startUploadProcess(
                    context = context,
                    imageUri = imageUri!!,
                    uploadViewModel = uploadViewModel,
                    onFinish = {
                        isUploading = false
                        pendingUpload = false
                        imageUri = null
                    }
                )
            } else {
                isUploading = false
                pendingUpload = false
                Toast.makeText(
                    context,
                    "Location permission required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    /* ---------------- UI ---------------- */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Capture Photo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageUri?.let {
            AsyncImage(
                model = it,
                contentDescription = "Captured Image",
                modifier = Modifier.size(220.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (imageUri == null) {
                    Toast.makeText(
                        context,
                        "Capture image first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                isUploading = true
                pendingUpload = true

                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isUploading
        ) {
            Text(if (isUploading) "Uploading..." else "Upload Place")
        }
    }
}

/* ---------------- SAFE UPLOAD FLOW ---------------- */

private fun startUploadProcess(
    context: Context,
    imageUri: Uri,
    uploadViewModel: UploadViewModel,
    onFinish: () -> Unit
) {
    getCurrentLocation(
        context = context,
        onLocation = { lat, lng ->
            val city = getCityName(context, lat, lng)

            uploadViewModel.uploadPlace(
                imageUri = imageUri,
                city = city,
                latitude = lat,
                longitude = lng,
                onSuccess = {
                    Toast.makeText(
                        context,
                        "Place uploaded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    onFinish()
                },
                onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    onFinish()
                }
            )
        },
        onError = {
            Toast.makeText(
                context,
                "Failed to get location",
                Toast.LENGTH_SHORT
            ).show()
            onFinish()
        }
    )
}

