
import androidx.compose.ui.window.ComposeUIViewController
import data.ApplicationComponent
import platform.UIKit.UIViewController

actual fun getPlatformName(): String = "iOS"

fun initialize() {
    ApplicationComponent.init()
}

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        App()
    }
}