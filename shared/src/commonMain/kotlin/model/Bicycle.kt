package model

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Bicycle(
    val id : Int = Random.Default.nextInt(),
    val bikename: String,
    val category: String,
    val description : String = "",
    val year : Int,
    val priceCent : Int,
    val imagePath : String = "",
)