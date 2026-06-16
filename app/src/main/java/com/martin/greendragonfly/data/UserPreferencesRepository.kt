package com.martin.greendragonfly.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

data class AccountInfo(
    val username: String,
    val password: String? = "",
//    val rememberPassword: Boolean = false,
    val lastUpdated: String? = null
)

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    fun accountInfo(): Flow<AccountInfo> = dataStore.data.map { preferences ->
        AccountInfo(
            username = preferences[ACC_USERNAME] ?: "",
            password = preferences[ACC_PASSWORD],
//            rememberPassword = preferences[ACC_REMEMBER_PASSWORD] ?: false,
            lastUpdated = preferences[ACC_LAST_UPDATED]
        )

    }

    suspend fun updateAccountInfo(acc: AccountInfo) {
        dataStore.edit { preferences ->
            preferences[ACC_USERNAME] = acc.username
            preferences[ACC_PASSWORD] = acc.password ?: ""
//            preferences[ACC_REMEMBER_PASSWORD] = acc.rememberPassword
            preferences[ACC_LAST_UPDATED] = acc.lastUpdated ?: ""
        }
    }

    private companion object {
        val ACC_USERNAME = stringPreferencesKey("acc_username")
        val ACC_PASSWORD = stringPreferencesKey("acc_password")
//        val ACC_REMEMBER_PASSWORD = booleanPreferencesKey("acc_remember_pwd")
        val ACC_LAST_UPDATED = stringPreferencesKey("acc_updated_at")
    }
}