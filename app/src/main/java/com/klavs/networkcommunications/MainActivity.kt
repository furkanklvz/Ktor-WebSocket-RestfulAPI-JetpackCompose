package com.klavs.networkcommunications

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.klavs.networkcommunications.ui.theme.NetworkCommunicationsTheme
import com.klavs.networkcommunications.uix.view.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetworkCommunicationsTheme {
                Navigation()
            }
        }
    }
}