package com.example.proyectofinaldisenomovil.data.repository.Remote

import android.net.Uri
import com.cloudinary.android.MediaManager
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CloudinaryRepository @Inject constructor(
    private val uploadPreset: String
) {
    suspend fun uploadImage(imageUri: Uri): String {
        return suspendCancellableCoroutine { continuation ->

            MediaManager.get()
                .upload(imageUri)
                .unsigned(uploadPreset)
                .callback(object : com.cloudinary.android.callback.UploadCallback {

                    override fun onStart(requestId: String?) {}

                    override fun onProgress(
                        requestId: String?,
                        bytes: Long,
                        totalBytes: Long
                    ) {}

                    override fun onSuccess(
                        requestId: String?,
                        resultData: MutableMap<Any?, Any?>?
                    ) {

                        val imageUrl =
                            resultData?.get("secure_url") as String

                        continuation.resume(imageUrl)
                    }

                    override fun onError(
                        requestId: String?,
                        error: com.cloudinary.android.callback.ErrorInfo?
                    ) {
                        continuation.resumeWithException(
                            Exception(error?.description)
                        )
                    }

                    override fun onReschedule(
                        requestId: String?,
                        error: com.cloudinary.android.callback.ErrorInfo?
                    ) {}
                })
                .dispatch()
        }
    }
}