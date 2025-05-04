
import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri

object BitmapUtils {
    fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): android.graphics.Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            println("getBitmapFromUri Exception: ${e.message}")
            null
        }
    }
}