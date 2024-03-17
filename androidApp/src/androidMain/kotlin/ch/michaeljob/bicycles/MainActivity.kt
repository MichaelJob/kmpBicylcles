package ch.michaeljob.bicycles

import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import data.ApplicationComponent
import data.ContextProvider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationComponent.init()
        ContextProvider().create(this) //was needed to init context

        setContent {
            MainView()
        }
    }
}