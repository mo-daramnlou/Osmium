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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.osmium.ui.model.ReceivedSignalUI

@Composable
fun LogScreen(receivedSignals: List<ReceivedSignalUI>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(receivedSignals, key = { it.id }) {
            ReceivedSignalListItem(it)
        }
    }
}

@Composable
fun ReceivedSignalListItem(receivedSignalUI: ReceivedSignalUI, isExpanded: Boolean = false) {
    Card(
        modifier = Modifier
            .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cell ID: ${receivedSignalUI.cellID}",
                    fontSize = 14.sp
                )
                Text(
                    text = "RSSI: ${receivedSignalUI.rssi}",
                    fontSize = 12.sp
                )
            }

            if (isExpanded) {
                Text(
                    text = "Distance: ${receivedSignalUI.distance}",
                    fontSize = 12.sp
                )
                Text(
                    text = "device X: ${receivedSignalUI.deviceX}",
                    fontSize = 12.sp
                )
                Text(
                    text = "device Y: ${receivedSignalUI.deviceY}",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun LogScreenPreview() {
    LogScreen(
        receivedSignals = listOf(
            ReceivedSignalUI(
                id = 1,
                cellID = 1,
                distance = 1,
                deviceX = 1.0,
                deviceY = 1.0,
                rssi = "1"
            ),
            ReceivedSignalUI(
                id = 2,
                cellID = 2,
                distance = 2,
                deviceX = 2.0,
                deviceY = 2.0,
                rssi = "2"
            )
        )
    )
}