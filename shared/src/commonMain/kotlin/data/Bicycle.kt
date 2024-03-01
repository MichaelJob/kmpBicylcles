package data

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Bicycle(
    val id : Int = Random.Default.nextInt(),
    val bikename: String = "",
    val category: String = "",
    val description : String = "",
    val year : Int = 0,
    val priceCent : Int = 0,
    val imagePath : String = "",
)