package com.example.osmium.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.osmium.ui.model.ReceivedSignalUI

@Composable
fun LogScreen(modifier: Modifier=Modifier,receivedSignals: List<ReceivedSignalUI>) {
    LazyColumn(modifier = modifier, reverseLayout = false) {
        items(receivedSignals, key = { it.id }) {
            ReceivedSignalListItem(it)
        }
    }
}

@Composable
fun ReceivedSignalListItem(receivedSignalUI: ReceivedSignalUI) {

    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .animateContentSize()
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp),
                text = receivedSignalUI.time,
                fontSize = 11.sp
            )
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 0.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Cell ID: ${receivedSignalUI.cellID}",
                    fontSize = 14.sp,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "RSSI: ${receivedSignalUI.rssi}",
                    fontSize = 14.sp,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 24.dp, top = 0.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ) {
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
                rssi = "1",
                time = "23:11:53"
            ),
            ReceivedSignalUI(
                id = 2,
                cellID = 2,
                distance = 2,
                deviceX = 2.0,
                deviceY = 2.0,
                rssi = "2",
                time = "23:11:53"
            )
        )
    )
}