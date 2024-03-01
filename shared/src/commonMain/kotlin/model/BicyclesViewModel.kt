package model

import data.Bicycle
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BicyclesUiState(
    val bicycles: List<Bicycle> = emptyList(),
    val currentBicycle: Bicycle? = null,
) {
  //  val categories = bicycles.map { it.category }.toSet()
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

    fun updateBicycles() {
        viewModelScope.launch {
            val bicycles = getBicycles()
            _uiState.update {
                it.copy(bicycles = bicycles)
            }
        }
    }

    private suspend fun getBicycles(): List<Bicycle> {
      //  return SupabaseService.getData()

        val bicycles = httpClient
            .get("https://www.michaeljob.ch/bicycles/bicycles.json")
            .body<List<Bicycle>>()
        return bicycles
    }

    private suspend fun setBicycles(): Boolean {
        val response: HttpResponse = httpClient.post("https://bknhmpxtmpputpcsxzyw.supabase.co") {
            contentType(ContentType.Application.Json)
            setBody(_uiState.value.bicycles)
        }
        return response.status==HttpStatusCode.OK
    }

    fun selectBicycle(bicycle: Bicycle) {
        _uiState.update {
            it.copy(currentBicycle = bicycle)
        }
    }
}