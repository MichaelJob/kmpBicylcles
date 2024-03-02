package model

import data.Bicycle
import data.SupabaseService
import dev.icerock.moko.mvvm.viewmodel.ViewModel
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

    private fun updateBicycles() {
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

    fun save() {
        viewModelScope.launch {
            uiState.value.currentBicycle?.let { saveBicycle(it) }
        }
        _uiState.update {
            it.copy(showEdit = false)
        }
    }

    private suspend fun saveBicycle(bicycle: Bicycle) {
        SupabaseService.storeNewBicycle(bicycle)
    }
}