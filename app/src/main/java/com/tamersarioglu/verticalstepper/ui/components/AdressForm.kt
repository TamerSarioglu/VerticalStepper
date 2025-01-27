package com.tamersarioglu.verticalstepper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tamersarioglu.verticalstepper.data.Address

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