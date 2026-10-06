package com.example.simpleenergy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusBadge(isOnline: Boolean, modifier: Modifier = Modifier) {
    val background = if (isOnline) Color(0xFFDFF6E8) else Color(0xFFFDE8E8)
    val textColor = if (isOnline) Color(0xFF0F5132) else Color(0xFF842029)
    val label = if (isOnline) "Online" else "Offline"

    Text(
        text = label,
        modifier = modifier
            .background(background, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        style = MaterialTheme.typography.labelMedium,
        color = textColor,
    )
}
