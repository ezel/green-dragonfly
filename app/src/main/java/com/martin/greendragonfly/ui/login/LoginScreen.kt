package com.martin.greendragonfly.ui.login

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.CookieManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.martin.greendragonfly.data.AccountInfo

import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state = rememberWebViewState("https://www.lqtedu.com/login.jsp")
    val navigator = rememberWebViewNavigator()
    val account = viewModel.account.collectAsState().value

    val showDialog = remember { mutableStateOf(false) }
    Column() {
        Button(onClick = { showDialog.value = true }) { Text("Show Account") }
        if (account.username == "" || showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                text = {
                    AccountForm(
                        account,
                        onSubmit = { newAcc ->
                            viewModel.updateAccount(newAcc)
                            showDialog.value = false
                        }
                    )
                },
                confirmButton = {},
                dismissButton = null
            )
        }
        if (!state.isLoading) {
            val currentUrl = state.lastLoadedUrl.toString()
            Text("Loading completed.\nURL: $currentUrl")
            if (currentUrl == "https://www.lqtedu.com/parent/selParStu.jsp") {
                val cookies = CookieManager.getInstance().getCookie(currentUrl)
                viewModel.setCookie(cookies)
                Log.d("LoginScreen", currentUrl)
                Log.d("LoginScreen", cookies.toString())
                Text("Got cookie:$cookies")
            }
        }
//        Text("Here is the webview:")
//        Text(text = "${state.pageTitle}")
//        Button(onClick = { navigator.navigateBack() }) { Text("Go Back") }
        Button(onClick = {
            navigator.evaluateJavaScript(
                """
                var event=new Event('input',{bubbles:true});
                var unameInput=document.getElementById("UNAME");unameInput.focus();unameInput.value="${account.username}";
                unameInput.dispatchEvent(event);
                var pwdInput=document.getElementById("UPWD");pwdInput.focus();pwdInput.value="${account.password}";
                pwdInput.dispatchEvent(event);
                """.trimIndent()
            )
        }) { Text("Send account") }
        WebView(
            state,
            navigator = navigator
        )
    }
}

@Composable
fun AccountForm(
    account: AccountInfo,
    onSubmit: (AccountInfo) -> Unit
) {
    val accountState = remember { mutableStateOf(account) }
    Column {
        Text("Username:")
        TextField(
            accountState.value.username,
            onValueChange = { newValue ->
                accountState.value = accountState.value.copy(username = newValue)
            })
        Text("Password:")
        TextField(
            accountState.value.password ?: "",
            onValueChange = { newValue ->
                accountState.value = accountState.value.copy(password = newValue)
            })
//        Row {
//            Checkbox(
//                accountState.value.rememberPassword,
//                onCheckedChange = {
//                    accountState.value =
//                        accountState.value.copy(rememberPassword = !accountState.value.rememberPassword)
//                })
//            Text("RememberPassword")
//        }
        Button(onClick = { onSubmit(accountState.value) }) { Text("Save") }
    }
}