package data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream


actual fun ByteArray.toImageBitmap(): ImageBitmap? {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
    return bitmap?.asImageBitmap()
}

actual fun ImageBitmap.toByteArray(): ByteArray? {
    val baos = ByteArrayOutputStream()
    this.asAndroidBitmap().compress(Bitmap.CompressFormat.JPEG, 75, baos)
    val byteArray = baos.toByteArray()
    baos.close()
    return byteArray
}