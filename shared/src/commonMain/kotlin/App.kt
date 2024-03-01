import androidx.compose.runtime.Composable
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import model.BicyclesViewModel
import ui.BicycleAppTheme
import ui.BicyclesPage


@Composable
fun App() {
    BicycleAppTheme {
        val bicyclesViewModel = getViewModel(Unit, viewModelFactory { BicyclesViewModel() })
        BicyclesPage(bicyclesViewModel)
    }
}


expect fun getPlatformName(): String