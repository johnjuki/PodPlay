package com.podplay.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.podplay.android.ui.PodPlayApp
import com.podplay.android.ui.theme.PodplayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PodplayTheme {
                PodPlayApp()
            }
        }
    }
}
