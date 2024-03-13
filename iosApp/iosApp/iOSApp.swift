import SwiftUI
import shared

@main
struct iOSApp: App {

    init() {
        Main_iosKt.initialize()
    }

	var body: some Scene {
		WindowGroup {
		    ZStack {
		        Color.white.ignoresSafeArea(.all) // status bar color
			        ContentView()
			}.preferredColorScheme(.light)
		}
	}
}