package com.martin.greendragonfly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.martin.greendragonfly.data.UserPreferencesRepository
import com.martin.greendragonfly.data.dataStore
import com.martin.greendragonfly.ui.login.LoginScreen
import com.martin.greendragonfly.ui.login.LoginViewModel.Companion.provideFactory
import com.martin.greendragonfly.ui.theme.GreenDragonflyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            GreenDragonflyTheme {
                Scaffold(modifier = Modifier.fillMaxSize().safeDrawingPadding()) { innerPadding ->
                    val repo = UserPreferencesRepository(dataStore = this.dataStore)
                    LoginScreen(viewModel(factory = provideFactory(repo)))
                }
            }
        }
    }
}
