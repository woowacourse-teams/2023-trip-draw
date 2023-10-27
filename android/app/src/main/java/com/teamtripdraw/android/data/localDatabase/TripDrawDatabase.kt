package com.teamtripdraw.android.data.localDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teamtripdraw.android.data.localDatabase.dao.AddressDao
import com.teamtripdraw.android.data.localDatabase.entity.AddressEntity

@Database(entities = [AddressEntity::class], version = 1, exportSchema = true)
abstract class TripDrawDatabase : RoomDatabase() {

    abstract fun addressDao(): AddressDao

    companion object {
        private const val DATABASE_NAME = "TripDrawDatabase"
        private var instance: TripDrawDatabase? = null

        fun getDatabase(context: Context): TripDrawDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): TripDrawDatabase {
            return Room.databaseBuilder(context, TripDrawDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}
