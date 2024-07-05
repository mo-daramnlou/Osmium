package com.example.osmium.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.osmium.data.model.PositionedCellEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PositionedCellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: List<PositionedCellEntity>)

    @Query("SELECT * FROM positioned_cell")
    fun getAllPositionedCells(): Flow<List<PositionedCellEntity>>

    @Query("DELETE FROM positioned_cell")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(item: List<PositionedCellEntity>) {
        clearAll()
        insertAll(item)
    }
}
