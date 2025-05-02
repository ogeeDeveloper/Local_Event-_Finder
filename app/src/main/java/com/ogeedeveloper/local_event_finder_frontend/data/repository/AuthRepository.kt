package com.ogeedeveloper.local_event_finder_frontend.data.repository

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.ogeedeveloper.local_event_finder_frontend.domain.model.LoginResponse
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // Firebase Auth methods
    suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<Result<FirebaseUser>>
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Flow<Result<FirebaseUser>>
    suspend fun signOut()
    suspend fun getCurrentUser(): FirebaseUser?
    
    // Google Sign-In methods
    fun getGoogleSignInIntent(): Intent
    suspend fun firebaseAuthWithGoogle(idToken: String): Flow<Result<FirebaseUser>>
    
    // Backend API methods
    suspend fun registerUserWithBackend(user: User): Flow<Result<LoginResponse>>
    suspend fun loginWithBackend(email: String, password: String): Flow<Result<LoginResponse>>
    suspend fun loginWithGoogleBackend(googleToken: String, email: String, name: String): Flow<Result<LoginResponse>>
}
