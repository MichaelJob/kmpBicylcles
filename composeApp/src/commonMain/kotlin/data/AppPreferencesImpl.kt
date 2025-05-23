package data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface AppPreferences {
    suspend fun isDarkModeEnabled(): Boolean
    suspend fun changeDarkMode(isDarkMode: Boolean): Preferences
    suspend fun isRegistered(): Boolean
    suspend fun changeRegistered(isRegistered: Boolean): Preferences
    suspend fun getEmail(): String
    suspend fun changeEmail(email: String): Preferences
    suspend fun getPassword(): String
    suspend fun changePassword(pw: String): Preferences
    suspend fun getUserUID(): String
    suspend fun changeUserUID(useruid: String): Preferences
}

internal class AppPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : AppPreferences {

    private companion object {
        private const val PREFS_TAG_KEY = "AppPreferences"
        private const val IS_DARK_MODE_ENABLED = "isDarkModeBoolean"
        private const val IS_REGISTERED = "isRegistered"
        private const val EMAIL_KEY = "email"
        private const val PW_KEY = "pw"
        private const val USERUID_KEY = "useruid"
    }

    private val darkModeKey = booleanPreferencesKey("$PREFS_TAG_KEY$IS_DARK_MODE_ENABLED")
    private val registeredKey = booleanPreferencesKey("$PREFS_TAG_KEY$IS_REGISTERED")
    private val emailKey = stringPreferencesKey("$PREFS_TAG_KEY$EMAIL_KEY")
    private val pwKey = stringPreferencesKey("$PREFS_TAG_KEY$PW_KEY")
    private val useruidKey = stringPreferencesKey("$PREFS_TAG_KEY$USERUID_KEY")

    override suspend fun isDarkModeEnabled() = dataStore.data.map { preferences: Preferences ->
        preferences[darkModeKey] == true
    }.first()

    override suspend fun changeDarkMode(isDarkMode : Boolean) = dataStore.edit { preferences: MutablePreferences ->
        preferences[darkModeKey] = isDarkMode
    }

    override suspend fun isRegistered() = dataStore.data.map { preferences: Preferences ->
        preferences[registeredKey] == true
    }.first()

    override suspend fun changeRegistered(isRegistered : Boolean) = dataStore.edit { preferences: MutablePreferences ->
        preferences[registeredKey] = isRegistered
    }

    override suspend fun getEmail(): String = dataStore.data.map { preferences: Preferences ->
        preferences[emailKey] ?: ""
    }.first()

    override suspend fun changeEmail(email: String)= dataStore.edit { preferences: MutablePreferences ->
        preferences[emailKey] = email
    }

    override suspend fun getPassword(): String = dataStore.data.map { preferences: Preferences ->
        preferences[pwKey] ?: ""
    }.first()

    override suspend fun changePassword(pw: String)= dataStore.edit { preferences: MutablePreferences ->
        preferences[pwKey] = pw
    }

    override suspend fun getUserUID(): String = dataStore.data.map { preferences: Preferences ->
        preferences[useruidKey] ?: ""
    }.first()

    override suspend fun changeUserUID(useruid: String): Preferences = dataStore.edit { preferences: MutablePreferences ->
        preferences[useruidKey] = useruid
    }
}