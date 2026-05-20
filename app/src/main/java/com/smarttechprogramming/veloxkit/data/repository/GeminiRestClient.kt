package com.smarttechprogramming.veloxkit.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiRestClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    suspend fun generateContent(apiKey: String, systemInstruction: String, prompt: String): String = withContext(Dispatchers.IO) {
        if (apiKey.trim().isEmpty()) {
            return@withContext "Error: Gemini API Key is empty. Please enter your API Key in Settings."
        }

        val requestUrl = "$BASE_URL?key=$apiKey"

        try {
            val requestBodyJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                if (systemInstruction.isNotEmpty()) {
                    val systemInstructionObj = JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", systemInstruction)
                            })
                        })
                    }
                    put("systemInstruction", systemInstructionObj)
                }

                val generationConfigObj = JSONObject().apply {
                    put("temperature", 0.7)
                }
                put("generationConfig", generationConfigObj)
            }

            val requestBodyString = requestBodyJson.toString()
            Log.d("GeminiRestClient", "Request Body: $requestBodyString")

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestBodyString.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            Log.d("GeminiRestClient", "Response: $responseBody")

            if (response.isSuccessful) {
                val responseJson = JSONObject(responseBody)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    if (content != null) {
                        val parts = content.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val part = parts.getJSONObject(0)
                            val text = part.optString("text", "")
                            if (text.isNotEmpty()) {
                                return@withContext text
                            }
                        }
                    }
                }
                "Error: Unexpected empty response from Gemini API."
            } else {
                val errorMsg = try {
                    JSONObject(responseBody).optJSONObject("error")?.optString("message") ?: "Unknown API error"
                } catch (e: Exception) {
                    "Unknown error response"
                }
                "API Error (HTTP ${response.code}): $errorMsg"
            }
        } catch (e: Exception) {
            Log.e("GeminiRestClient", "Exception in API Call", e)
            "Error: ${e.message ?: "Failed to connect to Gemini API"}"
        }
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
