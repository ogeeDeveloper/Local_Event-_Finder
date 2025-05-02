package com.ogeedeveloper.local_event_finder_frontend.data.upload

import android.content.Context
import android.net.Uri
import android.util.Log
import com.ogeedeveloper.local_event_finder_frontend.data.network.ApiConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for uploading images to the server
 */
@Singleton
class ImageUploadService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiConfig: ApiConfig,
    private val okHttpClient: OkHttpClient
) {
    /**
     * Uploads an image to the server and returns the URL of the uploaded image
     * @param imageUri The URI of the image to upload
     * @return The URL of the uploaded image, or null if upload failed
     */
    suspend fun uploadImage(imageUri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            // Convert URI to File
            val file = uriToFile(imageUri)
            if (file == null) {
                Log.e(TAG, "Failed to convert URI to file: $imageUri")
                return@withContext null
            }
            
            // Create multipart request
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.name, requestBody)
                .build()
                
            // Build the request
            val request = Request.Builder()
                .url("${apiConfig.getBaseUrl()}api/upload")
                .post(body)
                .build()
                
            // Execute the request
            val response = okHttpClient.newCall(request).execute()
            
            if (!response.isSuccessful) {
                Log.e(TAG, "Upload failed: ${response.code}")
                return@withContext null
            }
            
            // Parse the response
            val responseBody = response.body?.string()
            if (responseBody != null) {
                val json = JSONObject(responseBody)
                return@withContext json.optString("url")
            }
            
            return@withContext null
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading image", e)
            return@withContext null
        }
    }
    
    /**
     * Converts a URI to a File
     * @param uri The URI to convert
     * @return The File, or null if conversion failed
     */
    private suspend fun uriToFile(uri: Uri): File? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
            val fileName = "upload_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, fileName)
            
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            
            inputStream.close()
            return@withContext file
        } catch (e: Exception) {
            Log.e(TAG, "Error converting URI to file", e)
            return@withContext null
        }
    }
    
    companion object {
        private const val TAG = "ImageUploadService"
    }
}
