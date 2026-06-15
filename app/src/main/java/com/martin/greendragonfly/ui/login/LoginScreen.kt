package com.martin.greendragonfly.ui.login

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.CookieManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state = rememberWebViewState("https://www.lqtedu.com/login.jsp")
    val navigator = rememberWebViewNavigator()
    val account = viewModel.account.collectAsState()

    Column() {
        if (!state.isLoading) {
            val currentUrl = state.lastLoadedUrl.toString()
            Text("loading finished")
            Text(currentUrl)
            if (currentUrl == "https://www.lqtedu.com/parent/selParStu.jsp") {
                val cookies = CookieManager.getInstance().getCookie(currentUrl)
                viewModel.setCookie(cookies)
                Log.d("LoginScreen",currentUrl)
                Log.d("LoginScreen",cookies.toString())
                Text(text = cookies)
            }
        }
        Text("Here is the webview:")
        Text(text = "${state.pageTitle}")
        Button(onClick={navigator.navigateBack()}) { Text("Go Back") }
        Button(onClick={
            navigator.evaluateJavaScript("""
                var event=new Event('input',{bubbles:true});
                var unameInput=document.getElementById("UNAME");unameInput.focus();unameInput.value="${account.value.username}";
                unameInput.dispatchEvent(event);
                var pwdInput=document.getElementById("UPWD");pwdInput.focus();pwdInput.value="${account.value.password}";
                pwdInput.dispatchEvent(event);
                """.trimIndent()) }) { Text("send username") }
        WebView(
            state,
            navigator = navigator
        )
    }
}