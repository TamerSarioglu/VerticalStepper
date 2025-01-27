package com.tamersarioglu.verticalstepper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tamersarioglu.verticalstepper.ui.components.LoadingOverlay
import com.tamersarioglu.verticalstepper.ui.components.StepContent
import com.tamersarioglu.verticalstepper.ui.components.VerticalStepper
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