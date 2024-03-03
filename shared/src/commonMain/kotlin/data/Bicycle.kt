package data

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Bicycle(
    val id : Int = Random.Default.nextInt(),
    var bikename: String = "",
    var category: String = "",
    var description : String = "",
    var year : String = "",
    var price : String = "",
    var imagepath : String = "defaultbicycle.jpg",
){
    fun getBicycleImagePath() : String {
        if (imagepath=="defaultbicycle.jpg") {
            return "https://bknhmpxtmpputpcsxzyw.supabase.co/storage/v1/object/sign/bicycles-imgs/defaultbicycle.jpg?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1cmwiOiJiaWN5Y2xlcy1pbWdzL2RlZmF1bHRiaWN5Y2xlLmpwZyIsImlhdCI6MTcwOTQ1OTQ0NiwiZXhwIjoyNTczMzczMDQ2fQ.4FOP1sn0VKaerueL-HOlq00QCpeRqWKIEpOvRa8hYSE&t=2024-03-03T09%3A50%3A46.430Z"
        } else {
            return "https://www.michaeljob.ch/bicycles/${imagepath}"
        }
    }
}