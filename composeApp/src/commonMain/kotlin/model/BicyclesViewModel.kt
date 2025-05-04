package model


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import data.Bicycle
import data.SupabaseService
import data.coreComponent
import data.toByteArray
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
    val isLoading: Boolean = false,
)

class BicyclesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BicyclesUiState())
    val uiState = _uiState.asStateFlow()
    private var useruid: String = ""

    //camera/gallery based on medium.com/@qasimnawaz_70901/kotlin-multiplatform-compose-unified-image-capture-and-gallery-picker-with-permission-handling-8a8f8cc9cc82
    var newImageBitmap by mutableStateOf<ImageBitmap?>(null)
    var imageSourceOptionDialog by mutableStateOf(value = false)
    var launchCamera by mutableStateOf(value = false)
    var launchGallery by mutableStateOf(value = false)
    var launchSetting by mutableStateOf(value = false)
    var permissionRationalDialog by mutableStateOf(value = false)


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
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val bicycles = getBicycles().onEach { bicycle ->
                if(bicycle.imgpaths!=""){
                    val imgpaths = bicycle.imgpaths
                    val imageBitmaps = mutableListOf<Pair<String,ImageBitmap>>()
                    for (imgpath in imgpaths.split(";")){
                        val imageBitmap = SupabaseService.downloadImage(imgpath)?.toImageBitmap()
                        if (imageBitmap!=null) imageBitmaps.add(Pair(imgpath,imageBitmap))
                    }
                    bicycle.imagesBitmaps = imageBitmaps
                }
            }
            _uiState.update {
                it.copy(bicycles = bicycles, isLoading = false)
            }
        }
    }

    private suspend fun getBicycles(): List<Bicycle> {
        return SupabaseService.getData(useruid = useruid)
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
        if (useruid!=""){
            _uiState.update {
                it.copy(currentBicycle = Bicycle(useruid = useruid), showDetail = true, showEdit = true)
            }
        } else {
            println("useruid was empty!")
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
         val userid = SupabaseService.signUpNewUser(uiState.value.email, uiState.value.password)
         coreComponent.appPreferences.changeRegistered(true)
         _uiState.update {
             it.copy(
                 isLoggedIn = true,
                 isRegistered = true,
             )
         }
     }
 }

 fun signIn() {
     if (!uiState.value.isLoggedIn && uiState.value.email.isNotEmpty() && uiState.value.password.isNotEmpty()) {
         viewModelScope.launch {
             val logInMsgUserIdOrError = SupabaseService.signInWithEmail(uiState.value.email, uiState.value.password)
             if (logInMsgUserIdOrError.contains(other="error", ignoreCase = false)) {
                 _uiState.update {
                     it.copy(isLoggedIn = false, errorMsg = logInMsgUserIdOrError)
                 }
             } else {
                 _uiState.update {
                     it.copy(isLoggedIn = true, errorMsg = "")
                 }
                 useruid = logInMsgUserIdOrError
                 updateBicycles()
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

    fun saveNewImage(bitmap: ImageBitmap) {
        val updatedCurrentBicycle = uiState.value.currentBicycle
        updatedCurrentBicycle?.let {
            val imgcount = it.imagesBitmaps.size+1
            val newimagename = "$useruid-${it.bikename}-$imgcount.png"
            it.imagesBitmaps = it.imagesBitmaps.plus(Pair(newimagename,bitmap))
            it.imgpaths = it.imagesBitmaps.joinToString(";") { pair -> pair.first }

            viewModelScope.launch {
                uploadImage(newimagename, bitmap)
                saveBicycle(it)
            }

            _uiState.update { state ->
                state.copy(
                    currentBicycle =  it
                )
            }
        }
    }

    private suspend fun uploadImage(newimagename: String, bitmap: ImageBitmap) {
        val byteArray = bitmap.toByteArray()
        if (byteArray != null) {
            SupabaseService.uploadImage(newimagename, byteArray)
        }
    }

    fun deleteImage(image2delete: Pair<String, ImageBitmap>){
        uiState.value.currentBicycle?.let {
            it.imagesBitmaps = it.imagesBitmaps.minus(image2delete)
            it.imgpaths = it.imagesBitmaps.joinToString(";") { pair -> pair.first }
            viewModelScope.launch {
                SupabaseService.deleteImage(image2delete.first)
                saveBicycle(it)
                _uiState.update { state ->
                    state.copy(
                        currentBicycle =  it
                    )
                }
            }
        }
    }

    fun checkBicyclenameIsUnique(): Boolean {
        val currentName = uiState.value.currentBicycle?.bikename
        return uiState.value.bicycles
            .filter { it!=uiState.value.currentBicycle }
            .none{it.bikename==currentName}
    }

}