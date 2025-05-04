import androidx.compose.runtime.Composable
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import model.BicyclesViewModel
import ui.BicyclesUI
import ui.theme.BicycleAppTheme

@Composable
fun App() {
    BicycleAppTheme {
        val bicyclesViewModel = getViewModel(Unit, viewModelFactory { BicyclesViewModel() })
        BicyclesUI(bicyclesViewModel)
    }
}


expect fun getPlatformName(): String