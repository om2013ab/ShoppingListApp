package com.omarahmed.shoppinglist.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("token_store")

    private val tokenKey = stringPreferencesKey("token_key")

    suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[tokenKey] = token
        }
    }

    val getToken: Flow<String?> = context.dataStore.data
        .map {
            it[tokenKey] ?: ""
        }

    suspend fun clearDataStore(){
        context.dataStore.edit { it.clear() }
    }
}