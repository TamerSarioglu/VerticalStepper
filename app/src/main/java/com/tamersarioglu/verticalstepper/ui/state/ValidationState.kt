package com.tamersarioglu.verticalstepper.ui.state

data class ValidationState(
    val emailError: String? = null,
    val passwordError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val phoneError: String? = null,
    val billingAddressError: String? = null,
    val shippingAddressError: String? = null
) 