package com.martin.greendragonfly.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

data class AccountInfo(
    val username: String,
    val password: String? = "",
)

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    fun accountInfo(): Flow<AccountInfo> = dataStore.data.map { preferences ->
        AccountInfo(
            username = preferences[ACC_USERNAME] ?: "",
            password = preferences[ACC_PASSWORD],
        )
    }

    fun cookieLastUpdate(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[COOKIE_LAST_UPDATED]
    }

    suspend fun updateAccountInfo(acc: AccountInfo) {
        dataStore.edit { preferences ->
            preferences[ACC_USERNAME] = acc.username
            preferences[ACC_PASSWORD] = acc.password ?: ""
        }
    }

    suspend fun updateCookieLastUpdate(newValue: Date) {
        dataStore.edit { preferences ->
            preferences[COOKIE_LAST_UPDATED] = newValue.toString()
        }
    }

    private companion object {
        val ACC_USERNAME = stringPreferencesKey("acc_username")
        val ACC_PASSWORD = stringPreferencesKey("acc_password")

        val COOKIE_LAST_UPDATED = stringPreferencesKey("COOKIE_updated_at")
    }
}