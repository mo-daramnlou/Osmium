package com.example.osmium.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = PositionedCellEntity.TABLE_NAME
)
data class PositionedCellEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = CELL_ID_COLUMN)
    val cellID:Int,

    @ColumnInfo(name = CELL_X_COLUMN)
    val cellX:Double,

    @ColumnInfo(name = CELL_Y_COLUMN)
    val cellY:Double
){
    companion object{
        const val TABLE_NAME="positioned_cell"
        const val CELL_ID_COLUMN="cell_id"
        const val CELL_X_COLUMN="cell_x"
        const val CELL_Y_COLUMN="cell_y"
    }
}
