package com.tamersarioglu.verticalstepper.ui.state

import com.tamersarioglu.verticalstepper.data.Address

sealed class StepperEvent {
    data class UpdateStep(val step: Step) : StepperEvent()
    data class UpdateEmail(val email: String) : StepperEvent()
    data class UpdatePassword(val password: String) : StepperEvent()
    data class UpdateFirstName(val firstName: String) : StepperEvent()
    data class UpdateLastName(val lastName: String) : StepperEvent()
    data class UpdatePhone(val phone: String) : StepperEvent()
    data class UpdateBillingAddress(val address: Address) : StepperEvent()
    data class UpdateShippingAddress(val address: Address) : StepperEvent()
    data class UpdateUseShippingForBilling(val use: Boolean) : StepperEvent()
    data object Submit : StepperEvent()
}