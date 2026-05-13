package com.example.proyectofinaldisenomovil.data.repository.Remote

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.proyectofinaldisenomovil.data.repository.ImageRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class ImageRepositoryImpl @Inject constructor() : ImageRepository {

    override suspend fun uploadImage(uri: Uri): String? {
        return suspendCancellableCoroutine { continuation ->
            // Si configuraste un Upload Preset "Unsigned" en Cloudinary, 
            // cambia '.upload(uri)' por '.upload(uri).option("upload_preset", "tu_preset")'
            // para que sea más seguro y no requiera el api_secret.

            MediaManager.get().upload(uri).option("upload_preset", "mobile-preset")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Opcional: manejar inicio
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        // Opcional: manejar progreso
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String
                        if (continuation.isActive) {
                            continuation.resume(url)
                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }
                }).dispatch()
        }
    }
}
