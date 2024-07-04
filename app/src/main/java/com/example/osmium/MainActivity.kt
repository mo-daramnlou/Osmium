package com.example.osmium

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.osmium.ui.theme.OsmiumTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {


    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OsmiumTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }




        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, Array(1){android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE)
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, Array(1){android.Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION_CODE2)
        }

        getCellInfo()
        getDeviceCurrentLocation()
    }

    private fun getDeviceCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        lifecycleScope.launch {
            while (true) {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener {
                        Log.d("modarl", "lat: ${it.latitude} long: ${it.longitude}")
                    }.addOnFailureListener {
                    Log.d("modarl", "error: ${it.message}")
                }
                delay(3000)
            }
        }
    }

    private fun getCellInfo() {
        lifecycleScope.launch {
            while (true){
                Log.d("modar","getCellInfo")
                val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager? ?:return@launch
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.INTERNET
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val cellInfoList = telephonyManager.allCellInfo
                    val cellLocation= telephonyManager.cellLocation
                    for (cellInfo in cellInfoList) {
                        if (cellInfo is CellInfoLte) {

                            Log.d("modar", "ci: ${cellInfo.cellIdentity.ci}")
                            Log.d("modar", "mcc: ${cellInfo.cellIdentity.mccString}")
                            Log.d("modar", "mnc: ${cellInfo.cellIdentity.mncString}")
                            Log.d("modar", "tac: ${cellInfo.cellIdentity.tac}")
                        }
                    }
                }else{
                    Log.d("modar","permission not granted!")
                }
                delay(3000)

            }
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
                getCellInfo()
            }
        }
    }

    companion object{
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

