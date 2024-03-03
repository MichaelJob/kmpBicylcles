package model

import data.Bicycle
import data.SupabaseService
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BicyclesUiState(
    val bicycles: List<Bicycle> = emptyList(),
    val currentBicycle: Bicycle? = null,
    val showDetail: Boolean = false,
    val showEdit: Boolean = false,
)

class BicyclesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<BicyclesUiState>(BicyclesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateBicycles()
    }

    fun updateBicycles() {
        viewModelScope.launch {
            val bicycles = getBicycles()
            _uiState.update {
                it.copy(bicycles = bicycles)
            }
        }
    }

    private suspend fun getBicycles(): List<Bicycle> {
        return SupabaseService.getData()
    }

    fun selectBicycle(bicycle: Bicycle) {
        _uiState.update {
            it.copy(currentBicycle = bicycle, showDetail = true)
        }
    }

    fun showDetail(showDetail: Boolean) {
        _uiState.update {
            it.copy(showDetail = showDetail)
        }
    }

    fun showEdit(showEdit: Boolean = true) {
        _uiState.update {
            it.copy(showEdit = showEdit)
        }
    }

    fun createNewBicycle() {
        _uiState.update {
            it.copy(currentBicycle = Bicycle(), showDetail = true, showEdit = true)
        }
    }

    fun save() {
        viewModelScope.launch {
            uiState.value.currentBicycle?.let { saveBicycle(it) }
            updateStateToMainView()
        }
    }

    private suspend fun saveBicycle(bicycle: Bicycle) {
        SupabaseService.storeNewBicycle(bicycle)
    }

    fun remove() {
        viewModelScope.launch {
            uiState.value.currentBicycle?.let { removeBicycle(it) }
            updateStateToMainView()
        }
    }

    private suspend fun updateStateToMainView() {
        delay(100)
        _uiState.update {
            it.copy(showEdit = false, showDetail = false, currentBicycle = null)
        }
        updateBicycles()
    }

    private suspend fun removeBicycle(bicycle: Bicycle) {
        SupabaseService.deleteBicycle(bicycle.id)
    }
}