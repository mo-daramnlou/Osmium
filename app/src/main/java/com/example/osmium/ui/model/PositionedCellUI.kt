package com.example.osmium.ui.model

import com.example.osmium.data.model.PositionedCellEntity

data class PositionedCellUI(
    val cellID:Int,
    val cellX:Double,
    val cellY:Double
){
    companion object{
        fun fromPositionedCellEntity(positionedCellEntity: PositionedCellEntity): PositionedCellUI {
            return PositionedCellUI(
                cellID = positionedCellEntity.cellID,
                cellX = positionedCellEntity.cellX,
                cellY = positionedCellEntity.cellY
            )
        }
    }
}
