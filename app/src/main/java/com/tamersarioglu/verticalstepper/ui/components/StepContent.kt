package com.tamersarioglu.verticalstepper.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tamersarioglu.verticalstepper.NavigationButtons
import com.tamersarioglu.verticalstepper.ReviewSection
import com.tamersarioglu.verticalstepper.ui.state.Step
import com.tamersarioglu.verticalstepper.ui.state.StepperState
import com.tamersarioglu.verticalstepper.ui.state.StepperEvent

@Composable
fun StepContent(
    step: Step,
    state: StepperState,
    onEvent: (StepperEvent) -> Unit
) {
    when (step) {
        Step.ACCOUNT_DETAILS -> AccountDetailsStep(
            email = state.email,
            password = state.password,
            onEmailChange = { onEvent(StepperEvent.UpdateEmail(it)) },
            onPasswordChange = { onEvent(StepperEvent.UpdatePassword(it)) },
            onNext = { onEvent(StepperEvent.UpdateStep(Step.PERSONAL_INFO)) },
            emailError = state.validationState.emailError,
            passwordError = state.validationState.passwordError
        )
        
        Step.PERSONAL_INFO -> PersonalInfoStep(
            firstName = state.firstName,
            lastName = state.lastName,
            phone = state.phone,
            onFirstNameChange = { onEvent(StepperEvent.UpdateFirstName(it)) },
            onLastNameChange = { onEvent(StepperEvent.UpdateLastName(it)) },
            onPhoneChange = { onEvent(StepperEvent.UpdatePhone(it)) },
            onBack = { onEvent(StepperEvent.UpdateStep(Step.ACCOUNT_DETAILS)) },
            onNext = { onEvent(StepperEvent.UpdateStep(Step.BILLING_ADDRESS)) },
            firstNameError = state.validationState.firstNameError,
            lastNameError = state.validationState.lastNameError,
            phoneError = state.validationState.phoneError
        )
        
        Step.BILLING_ADDRESS -> AddressStep(
            title = "Billing Address",
            address = state.billingAddress,
            onAddressChange = { onEvent(StepperEvent.UpdateBillingAddress(it)) },
            onBack = { onEvent(StepperEvent.UpdateStep(Step.PERSONAL_INFO)) },
            onNext = { onEvent(StepperEvent.UpdateStep(Step.SHIPPING_ADDRESS)) },
            addressError = state.validationState.billingAddressError
        )
        
        Step.SHIPPING_ADDRESS -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Checkbox(
                        checked = state.useShippingForBilling,
                        onCheckedChange = { onEvent(StepperEvent.UpdateUseShippingForBilling(it)) }
                    )
                    Text("Same as billing address")
                }
                
                if (!state.useShippingForBilling) {
                    AddressStep(
                        title = "Shipping Address",
                        address = state.shippingAddress,
                        onAddressChange = { onEvent(StepperEvent.UpdateShippingAddress(it)) },
                        onBack = { onEvent(StepperEvent.UpdateStep(Step.BILLING_ADDRESS)) },
                        onNext = { onEvent(StepperEvent.UpdateStep(Step.PAYMENT_DETAILS)) },
                        addressError = state.validationState.shippingAddressError
                    )
                } else {
                    NavigationButtons(
                        onBack = { onEvent(StepperEvent.UpdateStep(Step.BILLING_ADDRESS)) },
                        onNext = { onEvent(StepperEvent.UpdateStep(Step.PAYMENT_DETAILS)) }
                    )
                }
            }
        }
        
        Step.PAYMENT_DETAILS -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    "Payment Information",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                // Add payment form fields here
                NavigationButtons(
                    onBack = { onEvent(StepperEvent.UpdateStep(Step.SHIPPING_ADDRESS)) },
                    onNext = { onEvent(StepperEvent.UpdateStep(Step.REVIEW)) }
                )
            }
        }
        
        Step.REVIEW -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    "Review Your Information",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                ReviewSection("Account", listOf(
                    "Email" to state.email
                ))
                ReviewSection("Personal", listOf(
                    "Name" to "${state.firstName} ${state.lastName}",
                    "Phone" to state.phone
                ))
                ReviewSection("Billing Address", listOf(
                    "Street" to state.billingAddress.street,
                    "City" to state.billingAddress.city,
                    "State" to state.billingAddress.state,
                    "ZIP" to state.billingAddress.zipCode,
                    "Country" to state.billingAddress.country
                ))
                if (!state.useShippingForBilling) {
                    ReviewSection("Shipping Address", listOf(
                        "Street" to state.shippingAddress.street,
                        "City" to state.shippingAddress.city,
                        "State" to state.shippingAddress.state,
                        "ZIP" to state.shippingAddress.zipCode,
                        "Country" to state.shippingAddress.country
                    ))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { onEvent(StepperEvent.UpdateStep(Step.PAYMENT_DETAILS)) }
                    ) {
                        Text("Back")
                    }
                    Button(
                        onClick = { onEvent(StepperEvent.Submit) }
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
} 