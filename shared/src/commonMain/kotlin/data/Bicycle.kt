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
    @EncodeDefault var framenumber: String = "",
    @EncodeDefault var category: String = "",
    @EncodeDefault var description : String = "",
    @EncodeDefault var year : String = "",
    @EncodeDefault var price : String = "",
    @EncodeDefault var imgpaths : String = "",
    @EncodeDefault var useruid : String = "",
){
    // transient property to hold the images, does not get serialized on updates
    @Transient var imagesBitmaps: List<Pair<String,ImageBitmap>> = emptyList()
}






expect fun ByteArray.toImageBitmap(): ImageBitmap?

expect fun ImageBitmap.toByteArray(): ByteArray?