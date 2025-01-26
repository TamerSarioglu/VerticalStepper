package com.tamersarioglu.verticalstepper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tamersarioglu.verticalstepper.ui.components.AccountDetailsStep
import com.tamersarioglu.verticalstepper.ui.components.AddressStep
import com.tamersarioglu.verticalstepper.ui.components.LoadingOverlay
import com.tamersarioglu.verticalstepper.ui.components.PersonalInfoStep
import com.tamersarioglu.verticalstepper.ui.state.StepperEvent
import com.tamersarioglu.verticalstepper.ui.theme.StepperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StepperTheme {
                StepperExample()
            }
        }
    }
}

@Composable
fun VerticalStepper(
    steps: List<String>,
    currentStep: Int,
    onStepClick: (Int) -> Unit,
    content: @Composable (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        steps.forEachIndexed { index, step ->
            StepItem(
                step = step,
                stepNumber = index + 1,
                isCompleted = index < currentStep,
                isActive = index == currentStep,
                showLine = index < steps.size - 1,
                onClick = { onStepClick(index) }
            )

            if (index == currentStep) {
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = shrinkVertically(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        content(index)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

data class Address(
    var street: String = "",
    var city: String = "",
    var state: String = "",
    var zipCode: String = "",
    var country: String = ""
)

@Composable
fun AddressForm(
    modifier: Modifier = Modifier,
    address: Address,
    onAddressChange: (Address) -> Unit,
    error: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = address.street,
            onValueChange = { onAddressChange(address.copy(street = it)) },
            label = { Text("Street Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = address.city,
                onValueChange = { onAddressChange(address.copy(city = it)) },
                label = { Text("City") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = address.state,
                onValueChange = { onAddressChange(address.copy(state = it)) },
                label = { Text("State") },
                modifier = Modifier.width(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = address.zipCode,
                onValueChange = { onAddressChange(address.copy(zipCode = it)) },
                label = { Text("ZIP Code") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = address.country,
                onValueChange = { onAddressChange(address.copy(country = it)) },
                label = { Text("Country") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StepperExample(
    viewModel: StepperViewModel = viewModel()
) {
    val state = viewModel.state.collectAsState().value

    LoadingOverlay(isLoading = state.isLoading)

    val steps = listOf(
        "Account Details",
        "Personal Information",
        "Billing Address",
        "Shipping Address",
        "Payment Details",
        "Review & Submit"
    )

    VerticalStepper(
        steps = steps,
        currentStep = state.currentStep,
        onStepClick = { step -> viewModel.onEvent(StepperEvent.UpdateStep(step)) }
    ) { step ->
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                (fadeIn(
                    animationSpec = tween(
                        durationMillis = 2000,
                        easing = FastOutSlowInEasing
                    )
                ) + slideInHorizontally(
                    animationSpec = tween(2000),
                    initialOffsetX = { fullWidth -> fullWidth }
                )).togetherWith(
                    fadeOut(
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = FastOutSlowInEasing
                        )
                    ) + slideOutHorizontally(
                        animationSpec = tween(2000),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
                )
            }
        ) { targetStep ->
            when (targetStep) {
                0 -> AccountDetailsStep(
                    email = state.email,
                    password = state.password,
                    onEmailChange = { viewModel.onEvent(StepperEvent.UpdateEmail(it)) },
                    onPasswordChange = { viewModel.onEvent(StepperEvent.UpdatePassword(it)) },
                    onNext = { viewModel.onEvent(StepperEvent.UpdateStep(1)) },
                    emailError = state.validationState.emailError,
                    passwordError = state.validationState.passwordError
                )
                1 -> PersonalInfoStep(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    phone = state.phone,
                    onFirstNameChange = { viewModel.onEvent(StepperEvent.UpdateFirstName(it)) },
                    onLastNameChange = { viewModel.onEvent(StepperEvent.UpdateLastName(it)) },
                    onPhoneChange = { viewModel.onEvent(StepperEvent.UpdatePhone(it)) },
                    onBack = { viewModel.onEvent(StepperEvent.UpdateStep(0)) },
                    onNext = { viewModel.onEvent(StepperEvent.UpdateStep(2)) },
                    firstNameError = state.validationState.firstNameError,
                    lastNameError = state.validationState.lastNameError,
                    phoneError = state.validationState.phoneError
                )
                2 -> AddressStep(
                    title = "Billing Address",
                    address = state.billingAddress,
                    onAddressChange = { viewModel.onEvent(StepperEvent.UpdateBillingAddress(it)) },
                    onBack = { viewModel.onEvent(StepperEvent.UpdateStep(1)) },
                    onNext = { viewModel.onEvent(StepperEvent.UpdateStep(3)) },
                    addressError = state.validationState.billingAddressError
                )
                3 -> {
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
                                onCheckedChange = { viewModel.onEvent(StepperEvent.UpdateUseShippingForBilling(it)) }
                            )
                            Text("Same as billing address")
                        }
                        if (!state.useShippingForBilling) {
                            AddressForm(
                                address = state.shippingAddress,
                                onAddressChange = { viewModel.onEvent(StepperEvent.UpdateShippingAddress(it)) }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        NavigationButtons(
                            onBack = { viewModel.onEvent(StepperEvent.UpdateStep(2)) },
                            onNext = { viewModel.onEvent(StepperEvent.UpdateStep(4)) }
                        )
                    }
                }
                4 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                    ) {
                        // Payment details form
                        Text(
                            "Payment Information",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        // Add payment form fields here
                        NavigationButtons(
                            onBack = { viewModel.onEvent(StepperEvent.UpdateStep(3)) },
                            onNext = { viewModel.onEvent(StepperEvent.UpdateStep(5)) }
                        )
                    }
                }
                5 -> {
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
                            OutlinedButton(onClick = { viewModel.onEvent(StepperEvent.UpdateStep(4)) }) {
                                Text("Back")
                            }
                            Button(onClick = { viewModel.onEvent(StepperEvent.Submit) }) {
                                Text("Submit")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewSection(
    title: String,
    items: List<Pair<String, String>>
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        items.forEach { (label, value) ->
            Row(
                modifier = Modifier.padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$label:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun NavigationButtons(
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(onClick = onBack) {
            Text("Back")
        }
        Button(onClick = onNext) {
            Text("Next")
        }
    }
}

@Composable
private fun StepItem(
    step: String,
    stepNumber: Int,
    isCompleted: Boolean,
    isActive: Boolean,
    showLine: Boolean,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Step number or check icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = when {
                            isActive -> MaterialTheme.colorScheme.primaryContainer
                            isCompleted -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stepNumber.toString(),
                        color = when {
                            isActive -> MaterialTheme.colorScheme.onPrimaryContainer
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = step,
                style = MaterialTheme.typography.bodyLarge,
                color = when {
                    isActive -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }

        if (showLine) {
            Box(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .width(2.dp)
                    .height(24.dp)
                    .background(
                        color = if (isCompleted)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
            )
        }
    }
}