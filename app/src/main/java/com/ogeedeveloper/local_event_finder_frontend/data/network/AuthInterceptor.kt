package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.data.storage.AuthLocalDataSource
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor to add authentication token to requests
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
) : Interceptor {
    
    // Store the bot protection cookie if we receive it
    private var botProtectionCookie: String? = null
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Create a request with all necessary headers
        val modifiedRequest = addHeadersToRequest(originalRequest)
        
        // Proceed with the request
        var response = chain.proceed(modifiedRequest)
        
        // Check if we got a bot protection response (status 409 with script content)
        if (response.code == 409) {
            val responseBody = response.body?.string()
            if (responseBody?.contains("document.cookie") == true) {
                // Extract the cookie from the response
                val cookiePattern = Pattern.compile("document\\.cookie\\s*=\\s*\"([^\"]+)\"")
                val matcher = cookiePattern.matcher(responseBody)
                
                if (matcher.find()) {
                    botProtectionCookie = matcher.group(1)
                    
                    // Close the previous response
                    response.close()
                    
                    // Create a new request with the bot protection cookie
                    val newRequest = modifiedRequest.newBuilder()
                        .header("Cookie", botProtectionCookie!!)
                        .build()
                    
                    // Retry the request with the cookie
                    response = chain.proceed(newRequest)
                }
            }
        }
        
        return response
    }
    
    private fun addHeadersToRequest(request: Request): Request {
        val requestBuilder = request.newBuilder()
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        
        // Add the bot protection cookie if we have it
        botProtectionCookie?.let {
            requestBuilder.header("Cookie", it)
        }
        
        // Get the token from local storage and add it if available
        val token = authLocalDataSource.getAuthToken()
        if (!token.isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }
        
        return requestBuilder.build()
    }
}
