import androidx.compose.ui.window.ComposeUIViewController
import com.michaeltchuang.example.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
