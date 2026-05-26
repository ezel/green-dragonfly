package com.martin.greendragonfly

import android.annotation.SuppressLint
import android.webkit.CookieManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewState


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(viewModel: String) {
    val state = rememberWebViewState("https://www.lqtedu.com/login.jsp")

    Column() {
        if (!state.isLoading) {
            val currentUrl = state.lastLoadedUrl.toString()

            if (currentUrl == "target url") {
                val cookies = CookieManager.getInstance().getCookie(currentUrl)
                Text(cookies)
            }
        }

        WebView(
            state,
            onCreated = { it.settings.javaScriptEnabled = true }
        )

    }