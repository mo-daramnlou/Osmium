package com.example.osmium.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.osmium.data.model.ReceivedSignalEntity.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
)
data class ReceivedSignalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COLUMN)
    val id:Int=0,

    @ColumnInfo(name = DEVICE_X_COLUMN)
    val deviceX:Double,

    @ColumnInfo(name = DEVICE_Y_COLUMN)
    val deviceY:Double,

    @ColumnInfo(name = CELL_ID_COLUMN)
    val cellID:Int,

    @ColumnInfo(name = DISTANCE_COLUMN)
    val distance:Double,

    @ColumnInfo(name = RSSI_COLUMN)
    val rssi:Int
    ){
    companion object{
        const val TABLE_NAME="received_signal"
        const val ID_COLUMN="id"
        const val DEVICE_X_COLUMN="device_x"
        const val DEVICE_Y_COLUMN="device_y"
        const val CELL_ID_COLUMN="cell_id"
        const val DISTANCE_COLUMN="distance"
        const val RSSI_COLUMN="rssi"

    }
}