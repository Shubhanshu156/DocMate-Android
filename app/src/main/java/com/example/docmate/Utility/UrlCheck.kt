package com.example.docmate.Utility

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Multipart
import java.io.File
import java.io.FileOutputStream
import java.net.MalformedURLException
import java.net.URL
import java.util.Calendar

class Utils {
    fun isURLValid(urlString: String): Boolean {
        return try {
            URL(urlString)
            true // If the URL is valid, no exception is thrown, and return true.
        } catch (e: MalformedURLException) {
            false // If the URL is invalid, catch the exception and return false.
        }
    }


    fun isGivenDateGreaterThanToday(
        givenYear: Int,
        givenMonth: Int,
        givenDay: Int,
        givenHour: Int,
        givenMinute: Int
    ): Boolean {
        val currentCalendar = Calendar.getInstance()
        val givenCalendar = Calendar.getInstance()

        givenCalendar.set(givenYear, givenMonth - 1, givenDay, givenHour, givenMinute)

        // Compare the given date with the current date
        return givenCalendar.timeInMillis > currentCalendar.timeInMillis
    }


    fun upload(context: Context, imageUri: Uri): MultipartBody.Part {
        val contentResolver: ContentResolver = context.contentResolver

        // Extract the file extension from the Uri
        val extension = getFileExtensionFromUri(contentResolver, imageUri)

        val filedir = context.applicationContext.filesDir
        val filename = "${System.currentTimeMillis()}${extension ?: ""}" // Append the extension to the name if it exists
        val file = File(filedir, filename)

        val inputStream = contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        return part
    }

    fun getFileExtensionFromUri(contentResolver: ContentResolver, fileUri: Uri): String? {
        return contentResolver.getType(fileUri)?.let { mimeType ->
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            if (!extension.isNullOrBlank()) {
                ".$extension"
            } else {
                null
            }
        }
    }
}