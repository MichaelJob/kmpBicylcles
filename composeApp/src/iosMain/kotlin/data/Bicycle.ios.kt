package data

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image


actual fun ByteArray.toImageBitmap(): ImageBitmap? {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}


actual fun ImageBitmap.toByteArray(): ByteArray? {
    return Image.makeFromBitmap(this.asSkiaBitmap())
        .encodeToData(EncodedImageFormat.PNG, 100)?.bytes
}