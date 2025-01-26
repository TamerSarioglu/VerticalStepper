package com.tamersarioglu.verticalstepper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.verticalstepper.ui.state.StepperEvent
import com.tamersarioglu.verticalstepper.ui.state.StepperState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.tamersarioglu.verticalstepper.utils.ValidationUtils
import com.tamersarioglu.verticalstepper.utils.ValidationResult
import com.tamersarioglu.verticalstepper.ui.state.ValidationState

class StepperViewModel : ViewModel() {
    private val _state = MutableStateFlow(StepperState())
    val state: StateFlow<StepperState> = _state.asStateFlow()

    private fun validateCurrentStep(): Boolean {
        when (_state.value.currentStep) {
            0 -> {
                val emailValidation = ValidationUtils.validateEmail(_state.value.email)
                val passwordValidation = ValidationUtils.validatePassword(_state.value.password)

                if (emailValidation is ValidationResult.Error || passwordValidation is ValidationResult.Error) {
                    _state.update {
                        it.copy(
                            validationState = ValidationState(
                                emailError = (emailValidation as? ValidationResult.Error)?.message,
                                passwordError = (passwordValidation as? ValidationResult.Error)?.message
                            )
                        )
                    }
                    return false
                }
            }

            1 -> {
                if (_state.value.firstName.isBlank() || _state.value.lastName.isBlank()) {
                    _state.update {
                        it.copy(
                            validationState = ValidationState(
                                firstNameError = if (_state.value.firstName.isBlank()) "First name is required" else null,
                                lastNameError = if (_state.value.lastName.isBlank()) "Last name is required" else null
                            )
                        )
                    }
                    return false
                }
                val phoneValidation = ValidationUtils.validatePhone(_state.value.phone)
                if (phoneValidation is ValidationResult.Error) {
                    _state.update {
                        it.copy(
                            validationState = ValidationState(
                                phoneError = phoneValidation.message
                            )
                        )
                    }
                    return false
                }
            }

            2 -> {
                val billingAddressValidation =
                    ValidationUtils.validateAddress(_state.value.billingAddress)
                if (billingAddressValidation is ValidationResult.Error) {
                    _state.update {
                        it.copy(
                            validationState = ValidationState(
                                billingAddressError = billingAddressValidation.message
                            )
                        )
                    }
                    return false
                }
            }

            3 -> {
                if (!_state.value.useShippingForBilling) {
                    val shippingAddressValidation =
                        ValidationUtils.validateAddress(_state.value.shippingAddress)
                    if (shippingAddressValidation is ValidationResult.Error) {
                        _state.update {
                            it.copy(
                                validationState = ValidationState(
                                    shippingAddressError = shippingAddressValidation.message
                                )
                            )
                        }
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun handleSubmit() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                delay(2000)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unknown error occurred"
                    )
                }
            }
        }
    }

    fun onEvent(event: StepperEvent) {
        viewModelScope.launch {
            when (event) {
                is StepperEvent.UpdateStep -> {
                    if (event.step > _state.value.currentStep && !validateCurrentStep()) {
                        return@launch
                    }
                    _state.update {
                        it.copy(
                            currentStep = event.step,
                            validationState = ValidationState() // Clear validation errors
                        )
                    }
                }

                is StepperEvent.UpdateEmail -> _state.update { it.copy(email = event.email) }
                is StepperEvent.UpdatePassword -> _state.update { it.copy(password = event.password) }
                is StepperEvent.UpdateFirstName -> _state.update { it.copy(firstName = event.firstName) }
                is StepperEvent.UpdateLastName -> _state.update { it.copy(lastName = event.lastName) }
                is StepperEvent.UpdatePhone -> _state.update { it.copy(phone = event.phone) }
                is StepperEvent.UpdateBillingAddress -> _state.update { it.copy(billingAddress = event.address) }
                is StepperEvent.UpdateShippingAddress -> _state.update { it.copy(shippingAddress = event.address) }
                is StepperEvent.UpdateUseShippingForBilling -> _state.update {
                    it.copy(
                        useShippingForBilling = event.use
                    )
                }

                StepperEvent.Submit -> handleSubmit()
            }
        }
    }
} 