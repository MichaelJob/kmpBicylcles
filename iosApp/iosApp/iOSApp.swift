import SwiftUI

@main
struct iOSApp: App {

    //FIXME: PreferencesDataStore
    // Initialize the shared module for Preferences
    init() { MainViewControllerKt.initialize() }

	var body: some Scene {
		WindowGroup {
		    ZStack {
		        Color.white.ignoresSafeArea(.all) // status bar color
			    ContentView()
			}.preferredColorScheme(.light)
		}
	}
}