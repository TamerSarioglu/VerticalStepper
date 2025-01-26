package com.tamersarioglu.verticalstepper.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tamersarioglu.verticalstepper.Address
import com.tamersarioglu.verticalstepper.AddressForm
import com.tamersarioglu.verticalstepper.NavigationButtons

@Composable
fun AddressStep(
    modifier: Modifier = Modifier,
    title: String,
    address: Address,
    onAddressChange: (Address) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    addressError: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        AddressForm(
            address = address,
            onAddressChange = onAddressChange,
            error = addressError
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        NavigationButtons(
            onBack = onBack,
            onNext = onNext
        )
    }
} 