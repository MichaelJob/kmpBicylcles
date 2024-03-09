
import androidx.compose.ui.window.ComposeUIViewController
import data.ApplicationComponent

actual fun getPlatformName(): String = "iOS"

fun MainViewController() {

    //FIXME: Initialize the application component for PreferencesDataStore
    fun initialize() {
        ApplicationComponent.init()
    }


    ComposeUIViewController {
        App()
    }
}