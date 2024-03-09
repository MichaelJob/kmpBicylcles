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
    val email: String = "",//budiman.job@gmail.com",
    val password: String = "",//pw123456789",
    val isLoggedIn: Boolean = false,
)

class BicyclesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BicyclesUiState())
    val uiState = _uiState.asStateFlow()


    init {
        //TODO: loadPrefs()
        if (uiState.value.email.isNotEmpty() && uiState.value.password.isNotEmpty()) {
            signIn()
        }
    }

    private fun updateBicycles() {
        viewModelScope.launch {
            val bicycles = getBicycles().onEach { bicycle ->
                bicycle.storagePath = SupabaseService.downloadImage(bicycle.imagepath)
            }
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

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun updatePW(pw: String) {
        _uiState.update {
            it.copy(password = pw)
        }
    }

    fun save() {
        viewModelScope.launch {
            uiState.value.currentBicycle?.let { currentBicycleToStore ->
                saveBicycle(currentBicycleToStore)
            }
            updateStateToMainView()
        }
    }

    private suspend fun saveBicycle(currentBicycleToStore: Bicycle) {
        val storedbicycle = SupabaseService.storeNewBicycle(currentBicycleToStore)
        //TODO: show toast bikename stored?
    }

    fun remove() {
        viewModelScope.launch {
            uiState.value.currentBicycle?.let { removeBicycle(it) }
            updateStateToMainView()
        }
    }

    private suspend fun updateStateToMainView() {
        delay(50)
        _uiState.update {
            it.copy(showEdit = false, showDetail = false, currentBicycle = null)
        }
        updateBicycles()
    }

    private suspend fun removeBicycle(bicycle: Bicycle) {
        SupabaseService.deleteBicycle(bicycle.id)
    }


    fun signUp() {
        viewModelScope.launch {
            println("uiState.value.email, uiState.value.password" + uiState.value.email + uiState.value.password)
            SupabaseService.signUpNewUser(uiState.value.email, uiState.value.password)
            _uiState.update {
                it.copy(isLoggedIn = true)
            }
        }
    }

    fun signIn() {
        if (!uiState.value.isLoggedIn) {
            viewModelScope.launch {
                SupabaseService.signInWithEmail(uiState.value.email, uiState.value.password)
                _uiState.update {
                    it.copy(isLoggedIn = true)
                }
                updateBicycles()
            }
        }
    }

    fun logout() {
        if (uiState.value.isLoggedIn) {
            viewModelScope.launch {
                SupabaseService.logout()
                _uiState.update {
                    it.copy(
                        bicycles = emptyList(),
                        currentBicycle = null,
                        showDetail = false,
                        showEdit = false,
                        password = "",
                        isLoggedIn = false
                    )
                }
            }
        }
    }


}