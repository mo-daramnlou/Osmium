package com.example.osmium.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.osmium.data.model.ReceivedSignalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceivedSignalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: List<ReceivedSignalEntity>)

    @Query("SELECT * FROM received_signal")
    fun getAllReceivedSignals(): Flow<List<ReceivedSignalEntity>>

    @Query("DELETE FROM received_signal")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(item: List<ReceivedSignalEntity>) {
        clearAll()
        insertAll(item)
    }
}
