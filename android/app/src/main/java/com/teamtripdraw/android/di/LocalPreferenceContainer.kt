package com.teamtripdraw.android.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.teamtripdraw.android.BuildConfig.ENCRYPTED_SHARED_PREFERENCE_MASTER_KEY_ALIAS

class LocalPreferenceContainer(context: Context) {
    private val masterKey = MasterKey.Builder(context, ENCRYPTED_SHARED_PREFERENCE_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val userIdentifyInfoPreference: SharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            "encryptedLoginInfoPreference",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

    val tripPreference: SharedPreferences =
        context.getSharedPreferences(TRIP_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val TRIP_PREFERENCE_FILE_NAME = "TRIP_PREFERENCE"
    }
}
