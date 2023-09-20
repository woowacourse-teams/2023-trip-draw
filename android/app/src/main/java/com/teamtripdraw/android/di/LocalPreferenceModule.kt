package com.teamtripdraw.android.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.teamtripdraw.android.BuildConfig.ENCRYPTED_SHARED_PREFERENCE_MASTER_KEY_ALIAS
import com.teamtripdraw.android.di.qualifier.TripPreference
import com.teamtripdraw.android.di.qualifier.UserIdentifyInfoPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalPreferenceModule {
    private const val TRIP_PREFERENCE_FILE_NAME = "TRIP_PREFERENCE"

    @Provides
    @Singleton
    @UserIdentifyInfoPreference
    fun providesUserIdentifyInfoPreference(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context, ENCRYPTED_SHARED_PREFERENCE_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            "encryptedLoginInfoPreference",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    @Provides
    @Singleton
    @TripPreference
    fun providesTripPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(TRIP_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
}
