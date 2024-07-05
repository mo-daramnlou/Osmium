package com.example.osmium

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.osmium.data.MainRepository
import com.example.osmium.data.db.ApplicationDatabase
import com.example.osmium.ui.screen.CellListScreen
import com.example.osmium.ui.screen.LogScreen
import com.example.osmium.ui.theme.OsmiumTheme


class MainActivity : ComponentActivity() {


    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(MainRepository(ApplicationDatabase.getInstance(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        viewModel.getDeviceCurrentLocation(this)
        viewModel.getCellInfo(this)

        setContent {
            OsmiumTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                var tabIndex by remember { mutableIntStateOf(0) }
                val tabs = listOf("Positioned cells", "Logs")

                Column(modifier = Modifier.fillMaxWidth()) {
                    TabRow(selectedTabIndex = tabIndex) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title) },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index },
                            )
                        }
                    }
                    when (tabIndex) {
                        0 -> CellListScreen(uiState.positionedCells)
                        1 -> LogScreen(uiState.receivedSignals)
                    }
                }

            }
        }




        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                Array(1) { Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_PERMISSION_CODE
            )
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                Array(1) { Manifest.permission.READ_PHONE_STATE },
                REQUEST_PERMISSION_CODE2
            )
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCellInfo()
            }
        }
    }

    companion object {
        const val REQUEST_PERMISSION_CODE = 100
        const val REQUEST_PERMISSION_CODE2 = 1002
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OsmiumTheme {
        Greeting("Android")
    }
}

