package com.martin.greendragonfly.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.martin.greendragonfly.data.AccountInfo
import com.martin.greendragonfly.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _cookie = MutableStateFlow<String>("")
    val cookie: StateFlow<String> = _cookie.asStateFlow()

    private val _account = MutableStateFlow(AccountInfo(username=""))
    val account: StateFlow<AccountInfo> = userPreferencesRepository.accountInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AccountInfo(username = "")
        )

    fun updateAccount(newValue: AccountInfo) {
        viewModelScope.launch {
            userPreferencesRepository.updateAccountInfo(newValue)
        }
        _account.value = newValue
    }

    fun setCookie(cookieStr: String) {
        Log.d(TAG, cookieStr)
        _cookie.value = cookieStr
    }

    companion object {
        const val TAG = "LoginViewModel"
        fun provideFactory(repo: UserPreferencesRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(repo) as T
                }
            }
    }
}
