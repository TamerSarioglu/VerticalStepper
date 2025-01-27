package com.tamersarioglu.verticalstepper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import com.tamersarioglu.verticalstepper.ui.components.LoadingOverlay
import com.tamersarioglu.verticalstepper.ui.components.StepContent
import com.tamersarioglu.verticalstepper.ui.state.Step
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

            AnimatedVisibility(
                visible = index == currentStep,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                ) {
                    content(index)
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

    val steps = Step.entries.map { it.title }

    VerticalStepper(
        steps = steps,
        currentStep = steps.indexOf(state.currentStep.title),
        onStepClick = { step -> 
            viewModel.onEvent(StepperEvent.UpdateStep(Step.entries[step]))
        }
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
            StepContent(
                step = Step.entries[targetStep],
                state = state,
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
fun ReviewSection(
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
                .padding(vertical = 8.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = animateColorAsState(
                            targetValue = when {
                                isActive -> MaterialTheme.colorScheme.primaryContainer
                                isCompleted -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ).value,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = isCompleted,
                    transitionSpec = {
                        (scaleIn() + fadeIn()).togetherWith(scaleOut() + fadeOut())
                    }
                ) { completed ->
                    if (completed) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = stepNumber.toString(),
                            color = animateColorAsState(
                                targetValue = when {
                                    isActive -> MaterialTheme.colorScheme.onPrimaryContainer
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            ).value
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = step,
                style = MaterialTheme.typography.bodyLarge,
                color = animateColorAsState(
                    targetValue = when {
                        isActive -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                ).value
            )
        }

        if (showLine) {
            Box(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .width(2.dp)
                    .height(24.dp)
                    .background(
                        color = animateColorAsState(
                            targetValue = if (isCompleted)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ).value
                    )
            )
        }
    }
}