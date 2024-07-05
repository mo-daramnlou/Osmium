package com.example.osmium.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.osmium.ui.model.PositionedCellUI

@Composable
fun CellListScreen(modifier: Modifier=Modifier,positionedCells: List<PositionedCellUI>) {
    LazyColumn(modifier = modifier) {
        items(positionedCells, key = { it.cellID }) {
            CellListItem(positionedCellUI = it)
        }
    }
}

@Preview
@Composable
fun CellListItem(
    positionedCellUI: PositionedCellUI = PositionedCellUI(
        cellID = 1345,
        cellX = 35.655454542134,
        cellY = 51.879451548646
    )
) {
    Card(
        modifier = Modifier
            .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = "Cell ID: ${positionedCellUI.cellID}",
                fontSize = 14.sp,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Row {
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = "X: ${positionedCellUI.cellX}",
                    fontSize = 12.sp
                )
                Text(
                    text = "Y: ${positionedCellUI.cellY}",
                    fontSize = 12.sp
                )
            }
        }
    }
}

