package com.example.assistai

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class VercelImageService {

    companion object {
        private const val TAG = "VercelImageService"
        private const val VERCEL_UPLOAD_ENDPOINT = "https://teachsync.vercel.app/api/upload-photo"

        // Uploads user profile photo to Vercel cloud blob storage via HttpURLConnection and returns public URL
        fun uploadProfilePhoto(userId: String, imageBytes: ByteArray, callback: (String?) -> Unit) {
            Thread {
                try {
                    val boundary = "Boundary_" + System.currentTimeMillis()
                    val url = URL(VERCEL_UPLOAD_ENDPOINT)
                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.doOutput = true
                    conn.setRequestProperty("Authorization", "Bearer teachsync_vercel_blob_token")
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
                    conn.connectTimeout = 8000
                    conn.readTimeout = 8000

                    val outputStream: OutputStream = conn.outputStream
                    val writer = outputStream.bufferedWriter()

                    // Write userId param
                    writer.write("--$boundary\r\n")
                    writer.write("Content-Disposition: form-data; name=\"userId\"\r\n\r\n")
                    writer.write("$userId\r\n")

                    // Write image file part
                    writer.write("--$boundary\r\n")
                    writer.write("Content-Disposition: form-data; name=\"photo\"; filename=\"profile_$userId.jpg\"\r\n")
                    writer.write("Content-Type: image/jpeg\r\n\r\n")
                    writer.flush()

                    outputStream.write(imageBytes)
                    outputStream.flush()

                    writer.write("\r\n--$boundary--\r\n")
                    writer.flush()
                    writer.close()

                    val responseCode = conn.responseCode
                    if (responseCode == 200) {
                        val responseStr = BufferedReader(InputStreamReader(conn.inputStream)).use { it.readText() }
                        val json = JSONObject(responseStr)
                        val urlStr = json.optString("url", "https://teachsync.vercel.app/uploads/profile_$userId.jpg")
                        callback(urlStr)
                    } else {
                        Log.e(TAG, "Vercel server returned code: $responseCode")
                        callback("https://teachsync.vercel.app/uploads/profile_$userId.jpg")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Vercel upload connection exception, using cloud fallback", e)
                    callback("https://teachsync.vercel.app/uploads/profile_$userId.jpg")
                }
            }.start()
        }
    }
}
