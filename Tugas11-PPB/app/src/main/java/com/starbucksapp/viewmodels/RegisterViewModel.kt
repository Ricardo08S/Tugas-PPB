package com.starbucksapp.viewmodels

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterViewModel : ViewModel() {
    var fullName by mutableStateOf("")
    var birthDate by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    var fullNameError by mutableStateOf<String?>(null)
    var birthDateError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    var showDatePickerDialog by mutableStateOf(false)

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun onBirthDateClicked() {
        showDatePickerDialog = true
    }

    fun onDateSelected(dateMillis: Long) {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        birthDate = formatter.format(Date(dateMillis))
        showDatePickerDialog = false
    }

    fun onDatePickerDismissed() {
        showDatePickerDialog = false
    }

    private fun validateInputs(): Boolean {
        fullNameError = null
        birthDateError = null
        emailError = null
        passwordError = null

        if (fullName.isBlank()) {
            fullNameError = "Full Name cannot be empty"
        }
        if (birthDate.isBlank()) {
            birthDateError = "Birth Date cannot be empty"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
        }
        if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
        }

        return fullNameError == null && birthDateError == null && emailError == null && passwordError == null
    }

    fun onRegisterClick(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (validateInputs()) {
            isLoading = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveAdditionalData(onSuccess, onError)
                    } else {
                        isLoading = false
                        emailError = task.exception?.message ?: "Registration Failed"
                    }
                }
        }
    }

    private fun saveAdditionalData(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val uid = auth.currentUser!!.uid
        val user = hashMapOf(
            "fullName" to fullName,
            "birthDate" to birthDate,
            "email" to email
        )

        firestore.collection("users").document(uid).set(user)
            .addOnSuccessListener {
                isLoading = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                isLoading = false
                onError("Failed to save data: ${e.message}")
            }
    }
}