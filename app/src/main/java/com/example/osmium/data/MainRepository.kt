package com.example.osmium.data

import com.example.osmium.data.db.ApplicationDatabase
import com.example.osmium.data.model.PositionedCellEntity
import com.example.osmium.data.model.ReceivedSignalEntity

class MainRepository(applicationDatabase: ApplicationDatabase) {

    private val receivedSignalDao = applicationDatabase.receivedSignalDao
    private val positionedCellDao = applicationDatabase.positionedCellDao

    fun getAllPositionedCells() = positionedCellDao.getAllPositionedCells()

    fun getAllReceivedSignals() = receivedSignalDao.getAllReceivedSignals()

    suspend fun insertReceivedSignals(receivedSignals: List<ReceivedSignalEntity>) {
        receivedSignalDao.insertAll(receivedSignals)
    }

    suspend fun replaceAllPositionedCells(positionedCells: List<PositionedCellEntity>) {
        positionedCellDao.replaceAll(positionedCells)
    }
}