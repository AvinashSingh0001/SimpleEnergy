package com.example.simpleenergy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.simpleenergy.presentation.navigation.AppNavigation
import com.example.simpleenergy.presentation.theme.SimpleEnergyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as SimpleEnergyApplication).container
        setContent {
            SimpleEnergyTheme {
                AppNavigation(container = container)
            }
        }
    }
}
