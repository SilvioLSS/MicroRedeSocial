package br.com.silvio.microredesocial.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream

class Base64Converter {
    companion object {
        fun drawableToString(drawable: Drawable): String {
            val bitmap = (drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
        }

        fun stringToBitmap(imageString: String): Bitmap {
            val bytes = Base64.decode(imageString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}