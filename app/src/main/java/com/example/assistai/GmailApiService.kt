package com.example.assistai

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class GmailApiService {

    companion object {
        private const val TAG = "GmailApiService"

        // Fetches real emails from Gmail API using OAuth Access Token
        fun fetchGmailMessages(accessToken: String, callback: (List<GmailMessage>) -> Unit) {
            Thread {
                try {
                    val url = URL("https://gmail.googleapis.com/gmail/v1/users/me/messages?q=booking+consultation")
                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "GET"
                    conn.setRequestProperty("Authorization", "Bearer $accessToken")
                    conn.connectTimeout = 5000
                    conn.readTimeout = 5000

                    if (conn.responseCode == 200) {
                        val responseStr = conn.inputStream.bufferedReader().use { it.readText() }
                        val json = JSONObject(responseStr)
                        val messagesArray = json.optJSONArray("messages") ?: org.json.JSONArray()
                        
                        val messageList = mutableListOf<GmailMessage>()
                        for (i in 0 until minOf(messagesArray.length(), 5)) {
                            val msgObj = messagesArray.getJSONObject(i)
                            val id = msgObj.optString("id")
                            val threadId = msgObj.optString("threadId")
                            
                            // Fetch individual message details
                            val detail = fetchMessageDetail(accessToken, id)
                            if (detail != null) {
                                messageList.add(detail)
                            }
                        }
                        callback(messageList)
                    } else {
                        Log.e(TAG, "Failed to fetch Gmail messages: ${conn.responseCode}")
                        callback(emptyList())
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error connecting to Gmail API", e)
                    callback(emptyList())
                }
            }.start()
        }

        private fun fetchMessageDetail(accessToken: String, messageId: String): GmailMessage? {
            try {
                val url = URL("https://gmail.googleapis.com/gmail/v1/users/me/messages/$messageId")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty("Authorization", "Bearer $accessToken")
                conn.connectTimeout = 3000
                conn.readTimeout = 3000

                if (conn.responseCode == 200) {
                    val responseStr = conn.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONObject(responseStr)
                    val snippet = json.optString("snippet", "")
                    
                    val headers = json.getJSONObject("payload").getJSONArray("headers")
                    var subject = "Student Booking Request"
                    var sender = "Student"

                    for (j in 0 until headers.length()) {
                        val h = headers.getJSONObject(j)
                        if (h.optString("name").equals("Subject", true)) {
                            subject = h.optString("value")
                        }
                        if (h.optString("name").equals("From", true)) {
                            sender = h.optString("value")
                        }
                    }
                    return GmailMessage(messageId, sender, subject, snippet)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching message detail for $messageId", e)
            }
            return null
        }
    }
}

data class GmailMessage(
    val id: String,
    val sender: String,
    val subject: String,
    val snippet: String
)
