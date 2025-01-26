package com.tamersarioglu.verticalstepper.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tamersarioglu.verticalstepper.NavigationButtons

@Composable
fun PersonalInfoStep(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
    phone: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    firstNameError: String? = null,
    lastNameError: String? = null,
    phoneError: String? = null,
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                label = { Text("First Name") },
                isError = firstNameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (firstNameError != null) {
                ErrorText(error = firstNameError)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = { Text("Last Name") },
                isError = lastNameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (lastNameError != null) {
                ErrorText(error = lastNameError)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = { Text("Phone Number") },
                isError = phoneError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (phoneError != null) {
                ErrorText(error = phoneError)
            }

            Spacer(modifier = Modifier.height(16.dp))

            NavigationButtons(
                onBack = onBack,
                onNext = onNext
            )
        }
    }
} 