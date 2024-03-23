package model


import data.Bicycle
import data.SupabaseService
import data.coreComponent
import data.toImageBitmap
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
    val email: String = "",
    val password: String = "",
    val errorMsg: String = "",
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
)

class BicyclesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BicyclesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val email = coreComponent.appPreferences.getEmail()
            val pw = coreComponent.appPreferences.getPassword()
            val isRegistered = coreComponent.appPreferences.isRegistered()
            _uiState.update {
                it.copy(email = email, password = pw, isRegistered = isRegistered)
            }
            signIn()
        }
    }

    private fun updateBicycles() {
        viewModelScope.launch {
            val bicycles = getBicycles().onEach { bicycle ->
                bicycle.imageBitmap = SupabaseService.downloadImage(bicycle.imagepath)?.toImageBitmap()
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
        viewModelScope.launch {
            coreComponent.appPreferences.changeEmail(email)
        }
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun updatePW(pw: String) {
        viewModelScope.launch {
            coreComponent.appPreferences.changePassword(pw)
        }
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
            coreComponent.appPreferences.changeRegistered(true)
            _uiState.update {
                it.copy(isLoggedIn = true, isRegistered = true)
            }
        }
    }

    fun signIn() {
        if (!uiState.value.isLoggedIn && uiState.value.email.isNotEmpty() && uiState.value.password.isNotEmpty()) {
            viewModelScope.launch {
                val logInErrorMsg = SupabaseService.signInWithEmail(uiState.value.email, uiState.value.password)
                if (logInErrorMsg=="") {
                    _uiState.update {
                        it.copy(isLoggedIn = true, errorMsg = "")
                    }
                    updateBicycles()
                } else {
                    _uiState.update {
                        it.copy(isLoggedIn = false, errorMsg = logInErrorMsg)
                    }
                }
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

    fun setRegistered() {
        _uiState.update {
            it.copy(
                isRegistered = true
            )
        }
    }


}