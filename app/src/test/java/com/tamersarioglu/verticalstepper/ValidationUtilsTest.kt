package com.tamersarioglu.verticalstepper

import com.tamersarioglu.verticalstepper.utils.ValidationResult
import com.tamersarioglu.verticalstepper.utils.ValidationUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationUtilsTest {
    @Test
    fun `validateEmail returns success for valid email`() {
        val result = ValidationUtils.validateEmail("test@example.com")
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun `validateEmail returns error for invalid email`() {
        val result = ValidationUtils.validateEmail("invalid-email")
        assertTrue(result is ValidationResult.Error)
    }

    @Test
    fun `validatePassword returns success for valid password`() {
        val result = ValidationUtils.validatePassword("Password123")
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun `validatePassword returns error for short password`() {
        val result = ValidationUtils.validatePassword("Pass1")
        assertTrue(result is ValidationResult.Error)
    }

    @Test
    fun `validatePhone returns success for valid phone number`() {
        val result = ValidationUtils.validatePhone("+1234567890")
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun `validatePhone returns error for invalid phone number`() {
        val result = ValidationUtils.validatePhone("123")
        assertTrue(result is ValidationResult.Error)
    }
} 