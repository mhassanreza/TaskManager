package com.bigint.taskmanager.presentation.screens.common_components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.launch

@Composable
fun RotatingFAB(onFabClick: () -> Unit) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 500), // Rotate in 0.5 seconds
        finishedListener = { rotationAngle = 0f }
    )

    val scale = remember { Animatable(1f) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope() // Create a coroutine scope

    FloatingActionButton(
        onClick = {
            if (!isAnimating) {
                isAnimating = true
                rotationAngle += 360f

                coroutineScope.launch {
                    // Bounce animation
                    scale.animateTo(0.8f, animationSpec = spring(stiffness = Spring.StiffnessHigh))
                    scale.animateTo(1f, animationSpec = spring(stiffness = Spring.StiffnessHigh))

                    // Ensure onFabClick is only called after all animations complete
                    onFabClick()
                    isAnimating = false
                }
            }
        },
        modifier = Modifier.scale(scale.value) // Apply bounce animation
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Rotating FAB",
            modifier = Modifier.rotate(rotation)
        )
    }
}
