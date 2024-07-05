package com.example.osmium.ui.model

import com.example.osmium.data.model.ReceivedSignalEntity
import java.net.IDN

data class ReceivedSignalUI(
    val id:Int,
    val deviceX:Double,
    val deviceY:Double,
    val cellID:Int,
    val distance:Int,
    val rssi:String
){
    companion object{
        fun fromReceivedSignalEntity(receivedSignalEntity: ReceivedSignalEntity):ReceivedSignalUI{
            return ReceivedSignalUI(
                id = receivedSignalEntity.id,
                deviceX = receivedSignalEntity.deviceX,
                deviceY = receivedSignalEntity.deviceY,
                cellID = receivedSignalEntity.cellID,
                distance = receivedSignalEntity.distance.toInt(),
                rssi = receivedSignalEntity.rssi.toString()
            )
        }
    }
}
