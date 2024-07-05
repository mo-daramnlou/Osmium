package com.example.osmium

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.osmium.data.MainRepository
import com.example.osmium.data.db.ApplicationDatabase
import com.example.osmium.ui.screen.CellListScreen
import com.example.osmium.ui.screen.LogScreen
import com.example.osmium.ui.theme.OsmiumTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {


    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(MainRepository(ApplicationDatabase.getInstance(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                Array(2) { Manifest.permission.ACCESS_FINE_LOCATION; Manifest.permission.READ_PHONE_STATE },
                0
            )
        }

        viewModel.getDeviceCurrentLocation(this)
        viewModel.getCellInfo(this)

        lifecycleScope.launch {
            viewModel.uiState.map { it.permissionNotGrantedToast }.collectLatest {
                if (it) {
                    Toast.makeText(this@MainActivity, "Permission not granted. Please grant permissions manually", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.updateUIState { state -> state.copy(permissionNotGrantedToast = false) }
                }
            }
        }

        setContent {
            OsmiumTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                var pageIndex by remember { mutableIntStateOf(0) }

                Column(modifier = Modifier.fillMaxWidth()) {
                    when (pageIndex) {
                        0 -> CellListScreen(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f), uiState.positionedCells
                        )

                        1 -> LogScreen(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f), uiState.receivedSignals
                        )
                    }
                    BottomNavigationBar(
                        selectedPage = pageIndex,
                        onItemSelected = { pageIndex = it }
                    )
                }

            }
        }

    }

    @Composable
    fun BottomNavigationBar(selectedPage: Int, onItemSelected: (Int) -> Unit) {
        NavigationBar {

            NavigationBarItem(
                selected = selectedPage == BottomNavItem.PositionedCells.number,
                onClick = { onItemSelected(BottomNavItem.PositionedCells.number) },
                icon = { Text(text = BottomNavItem.PositionedCells.label) },
            )

            NavigationBarItem(
                selected = selectedPage == BottomNavItem.Logs.number,
                onClick = { onItemSelected(BottomNavItem.Logs.number) },
                icon = { Text(text = BottomNavItem.Logs.label) },
            )

        }
    }
}

sealed class BottomNavItem(val number: Int, val label: String) {
    data object PositionedCells : BottomNavItem(0, "Positioned Cells")
    data object Logs : BottomNavItem(1, "Logs")
}

