package com.example.osmium.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.osmium.data.model.PositionedCellEntity
import com.example.osmium.data.model.ReceivedSignalEntity

const val DB_VERSION = 1

@Database(
    entities = [
        ReceivedSignalEntity::class,
        PositionedCellEntity::class,
    ],
    version = DB_VERSION,
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract val receivedSignalDao: ReceivedSignalDao
    abstract val positionedCellDao: PositionedCellDao

    companion object {
        private const val DB_NAME = "cells.db"

        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getInstance(context: Context): ApplicationDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ApplicationDatabase::class.java,
                        DB_NAME
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
