package com.tamersarioglu.verticalstepper.ui.state

import com.tamersarioglu.verticalstepper.Address

data class StepperState(
    val currentStep: Step = Step.ACCOUNT_DETAILS,
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val billingAddress: Address = Address(),
    val shippingAddress: Address = Address(),
    val useShippingForBilling: Boolean = false,
    val validationState: ValidationState = ValidationState(),
    val isLoading: Boolean = false,
    val error: String? = null
)