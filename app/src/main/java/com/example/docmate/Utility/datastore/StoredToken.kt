package com.example.docmate.Utility.datastore
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoredToken constructor(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

    companion object {
        val TOKEN_KEY = stringPreferencesKey(name = "token")
        val USER_TYPE_KEY = stringPreferencesKey(name = "user_type")
        val USER_ID_KEY = stringPreferencesKey(name = "user_id")
    }

    suspend fun storeToken(token: String, userType: String, userid: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_TYPE_KEY] = userType
            preferences[USER_ID_KEY] = userid
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    fun getUserType(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_TYPE_KEY]
        }
    }

    fun getUserID(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }
}
