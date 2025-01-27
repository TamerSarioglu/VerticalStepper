package com.tamersarioglu.verticalstepper.utils

import com.tamersarioglu.verticalstepper.data.Address

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

object ValidationUtils {
    fun validateEmail(email: String): ValidationResult {
        return if (email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("Invalid email format")
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.length < 8 -> ValidationResult.Error("Password must be at least 8 characters")
            !password.any { it.isDigit() } -> ValidationResult.Error("Password must contain at least one number")
            !password.any { it.isUpperCase() } -> ValidationResult.Error("Password must contain at least one uppercase letter")
            else -> ValidationResult.Success
        }
    }

    fun validatePhone(phone: String): ValidationResult {
        return if (phone.matches(Regex("^[+]?[0-9]{10,13}$"))) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("Invalid phone number format")
        }
    }

    fun validateAddress(address: Address): ValidationResult {
        return when {
            address.street.isBlank() -> ValidationResult.Error("Street address is required")
            address.city.isBlank() -> ValidationResult.Error("City is required")
            address.state.isBlank() -> ValidationResult.Error("State is required")
            address.zipCode.isBlank() -> ValidationResult.Error("ZIP code is required")
            address.country.isBlank() -> ValidationResult.Error("Country is required")
            else -> ValidationResult.Success
        }
    }
} 