package com.example.pokemon_compose.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class ImageDownloadService(
    context: Context
) {
    private val cacheDir = File(context.cacheDir, "pokemon_images")

    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    suspend fun downloadImage(imageUrl: String, pokemonId: Int): /*String*/File? =
        withContext(Dispatchers.IO) {
            try {
                val fileName = "pokemon_${pokemonId}.png"
                val imageFile = File(cacheDir, fileName)

            if (imageFile.exists()) {
                return@withContext imageFile/*.absolutePath*/
            }

            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.doInput = true
            connection.connect()

            val inputStream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (bitmap != null) {
                val fileOutputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.close()
                bitmap.recycle()

                return@withContext imageFile/*.absolutePath*/
            }

            return@withContext null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    fun getImageFile(pokemonId: Int): File {
        return File(cacheDir, "pokemon_${pokemonId}.png")
    }

    fun isImageCached(pokemonId: Int): Boolean {
        return getImageFile(pokemonId).exists()
    }
}