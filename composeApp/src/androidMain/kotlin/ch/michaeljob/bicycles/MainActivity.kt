package ch.michaeljob.bicycles

import MainView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import data.ApplicationComponent
import data.ContextProvider


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationComponent.init()
        ContextProvider().create(this) //was needed to init context


        setContent {
            MainView()
        }
    }
}