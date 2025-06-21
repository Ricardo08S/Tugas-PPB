package com.starbucksapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class UserProfile(
    val fullName: String = "",
    val email: String = "",
    val birthDate: String = ""
)

class ProfileViewModel : ViewModel() {
    var userProfile by mutableStateOf<UserProfile?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        isLoading = true
        errorMessage = null

        val userId = auth.currentUser?.uid
        if (userId == null) {
            errorMessage = "User not logged in."
            isLoading = false
            return
        }

        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profile = document.toObject(UserProfile::class.java)
                    userProfile = profile
                } else {
                    errorMessage = "No profile data found."
                }
                isLoading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = "Failed to fetch data: ${exception.message}"
                isLoading = false
            }
    }
}