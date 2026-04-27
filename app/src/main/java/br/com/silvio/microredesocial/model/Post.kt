package br.com.silvio.microredesocial.model

import android.graphics.Bitmap
import com.google.firebase.Timestamp

data class Post(
    val descricao: String,
    val imagem: Bitmap,
    val cidade: String = "",
    val data: Timestamp? = null,
    val autor: String = ""
)