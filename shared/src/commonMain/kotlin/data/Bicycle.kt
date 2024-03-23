package data

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.random.Random

@Serializable
data class Bicycle @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val id : Int = Random.Default.nextInt(),
    @EncodeDefault var bikename: String = "",
    @EncodeDefault var category: String = "",
    @EncodeDefault var description : String = "",
    @EncodeDefault var year : String = "",
    @EncodeDefault var price : String = "",
    @EncodeDefault var imagepath : String = "defaultbicycle.jpg",
){
    @Transient var imageBitmap: ImageBitmap? = null // transient property to hold the image, does not get serialized on updates
}


expect fun ByteArray.toImageBitmap(): ImageBitmap?

expect fun ImageBitmap.toByteArray(): ByteArray?