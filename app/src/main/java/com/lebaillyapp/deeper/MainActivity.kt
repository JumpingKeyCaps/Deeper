package com.lebaillyapp.deeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lebaillyapp.deeper.cockpitUiKit.components.DebugComponentScreen
import com.lebaillyapp.deeper.ui.theme.DeeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeperTheme {
                DebugComponentScreen()
            }
        }
    }
}
