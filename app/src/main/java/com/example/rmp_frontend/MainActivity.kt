package com.example.rmp_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.example.rmp_frontend.presentation.di.AppContainer
import com.example.rmp_frontend.presentation.navigation.AppNavGraph
import com.example.rmp_frontend.ui.theme.RMPfrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RMPfrontendTheme {
                val appContainer = remember { AppContainer(applicationContext) }
                AppNavGraph(appContainer)
            }
        }
    }
}
