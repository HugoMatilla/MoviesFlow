package com.hugomatilla.moviesflow.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import hu.autsoft.krate.Krate
import hu.autsoft.krate.booleanPref
import hu.autsoft.krate.stringPref

class LocalCache(context: Context) : Krate {
    override val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs",
        "masterKeyAlias",
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var sessionId by stringPref("sessionId", "")
    var randomMovie by stringPref("randomMovie", "")
    var isDarkMode by booleanPref("isDarkMode", true)

}