package com.example.osmium

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.CellInfo
import android.telephony.CellInfoLte
import android.telephony.TelephonyManager
import android.text.format.DateFormat
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.osmium.business.circularLateration.estimatePosition
import com.example.osmium.business.rssiDistanceCalculator.RssiDistanceCalculator
import com.example.osmium.data.MainRepository
import com.example.osmium.data.model.PositionedCellEntity
import com.example.osmium.data.model.ReceivedSignalEntity
import com.example.osmium.ui.model.PositionedCellUI
import com.example.osmium.ui.model.ReceivedSignalUI
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getReceivedSignals()
        getPositionedCells()

        calculateCellPositions()
    }

    fun updateUIState(update: (MainActivityUIState) -> MainActivityUIState) {
        _uiState.update { update(it) }
    }

    fun getDeviceCurrentLocation(activity: MainActivity) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener {
                _uiState.update { state ->
                    state.copy(
                        deviceLocation = Pair(
                            it.latitude,
                            it.longitude
                        )
                    )
                }
            }.addOnFailureListener {
                Log.d(TAG, "error: ${it.message}")
            }
    }

    @SuppressLint("MissingPermission")
    fun getCellInfo(activity: MainActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            val telephonyManager =
                activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
                    ?: return@launch

            while (true) {
                if (arePermissionsGranted(activity)) {

                    val receivedSignalEntities = mutableListOf<ReceivedSignalEntity>()

                    val deviceX = uiState.value.deviceLocation?.first
                    val deviceY = uiState.value.deviceLocation?.second

                    if (deviceX != null && deviceY != null) {
                        val cellInfoList = telephonyManager.allCellInfo
                        for (cellInfo in cellInfoList) {
                            if (cellInfo is CellInfoLte) {
                                if (cellInfo.cellIdentity.ci == 0) continue

                                val rssi = cellInfo.cellSignalStrength.rssi
                                if (rssi == CellInfo.UNAVAILABLE) continue

                                val distance =
                                    RssiDistanceCalculator.calculateDistance(cellInfo.cellSignalStrength.rssi.toDouble())

                                receivedSignalEntities.add(
                                    ReceivedSignalEntity(
                                        cellID = cellInfo.cellIdentity.ci,
                                        deviceX = deviceX,
                                        deviceY = deviceY,
                                        distance = distance,
                                        rssi = cellInfo.cellSignalStrength.rssi,
                                        timeMillis = System.currentTimeMillis()
                                    )
                                )
                            }
                        }
                        mainRepository.insertReceivedSignals(receivedSignalEntities)
                    }

                } else {
                    _uiState.update { state ->
                        state.copy(permissionNotGrantedToast = true)
                    }
                }
                delay(10000)
            }
        }
    }

    private fun arePermissionsGranted(activity: MainActivity): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.INTERNET
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getReceivedSignals() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getAllReceivedSignals()
                .collectLatest { receivedSignalEntities ->
                    val receivedSignalUIs =
                        receivedSignalEntities.map { ReceivedSignalUI.fromReceivedSignalEntity(it) }
                    _uiState.update { state ->
                        state.copy(receivedSignals = receivedSignalUIs)
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun calculateCellPositions() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getAllReceivedSignals()
                .mapLatest { receivedSignalEntities ->
                    receivedSignalEntities.groupBy { it.cellID }.map {
                        if (it.value.size < 3) return@map null

                        val positions = Array(it.value.size) { DoubleArray(2) }
                        for (i: Int in positions.indices) {
                            positions[i][0] = it.value[i].deviceX
                            positions[i][1] = it.value[i].deviceY
                        }

                        positions.forEach { it.forEach { it2 -> Log.d("modar","$it2") } }

                        val distances = DoubleArray(it.value.size)
                        for (i: Int in distances.indices) {
                            distances[i] = it.value[i].distance
                        }

                        distances.forEach { Log.d("modar","$it") }

                        var cellPosition: DoubleArray
                        try {
                            cellPosition = estimatePosition(positions, distances)
                        } catch (e: Exception) {
                            Log.d(TAG, "error: ${e.message}")
                            cellPosition = estimatePosition(
                                positions.sliceArray(IntRange(0, 2)),
                                distances.sliceArray(IntRange(0, 2))
                            )
                        }

                        PositionedCellEntity(
                            cellID = it.key,
                            cellX = cellPosition[0],
                            cellY = cellPosition[1]
                        )
                    }
                }
                .collectLatest { positionedCellEntities ->
                    mainRepository.replaceAllPositionedCells(positionedCellEntities.filterNotNull())
                }
        }
    }

    private fun getPositionedCells() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getAllPositionedCells()
                .collectLatest { positionedCellEntities ->
                    val positionedCellUIs =
                        positionedCellEntities.map { PositionedCellUI.fromPositionedCellEntity(it) }
                    _uiState.update { state ->
                        state.copy(positionedCells = positionedCellUIs)
                    }
                }
        }
    }


    companion object {
        const val TAG = "MainViewModel"
    }
}

data class MainActivityUIState(
    val deviceLocation: Pair<Double, Double>? = null,
    val positionedCells: List<PositionedCellUI> = emptyList(),
    val receivedSignals: List<ReceivedSignalUI> = emptyList(),
    val permissionNotGrantedToast: Boolean = false
)