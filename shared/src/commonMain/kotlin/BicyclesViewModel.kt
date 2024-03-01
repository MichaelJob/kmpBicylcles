import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Bicycle

data class BicyclesUiState(
    val bicycles: List<Bicycle> = emptyList(),
    val selectedCategory: String? = null
) {
    val categories = bicycles.map { it.category }.toSet()
    val selectedImages = bicycles.filter { it.category == selectedCategory }
}

class BicyclesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<BicyclesUiState>(BicyclesUiState())
    val uiState = _uiState.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    init {
        updateBicycles()
    }

    override fun onCleared() {
        httpClient.close()
    }

    fun selectCategory(category: String) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
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
        val bicycles = httpClient
            .get("https://www.michaeljob.ch/bicycles/bicycles.json")
            .body<List<Bicycle>>()
        return bicycles
    }
}