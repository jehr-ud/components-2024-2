package com.ud.memorygame.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ud.memorygame.model.logic.Level
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LevelViewModel : ViewModel() {
    private val _levelState = MutableStateFlow<String?>(null)
    val levelState: StateFlow<String?> get() = _levelState

    fun fetchPlayerLevel(playerId: String) {
        viewModelScope.launch {
            try {
                // Use the IP address for your local development
                val ipAddress = "10.0.2.2" // For Android emulator
                val url = URL("http://$ipAddress:3000/api/v1/levels/$playerId")

                Log.e("LevelViewModel", "Attempting to connect to URL: $url")

                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000  

                try {
                    Log.e("LevelViewModel", "Response Code: ${connection.responseCode}")
                    Log.e("LevelViewModel", "Response Message: ${connection.responseMessage}")

                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = connection.inputStream
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        val response = StringBuilder()
                        var line: String?

                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        reader.close()

                        Log.e("LevelViewModel", "Full Response: $response")

                        val jsonResponse = JSONObject(response.toString())

                        // Log each field from the JSON
                        Log.e("LevelViewModel", "JSON _id: ${jsonResponse.optString("_id")}")
                        Log.e("LevelViewModel", "JSON player_uuid: ${jsonResponse.optString("player_uuid")}")
                        Log.e("LevelViewModel", "JSON level: ${jsonResponse.optString("level")}")

                        val level = jsonResponse.optString("level", "low")

                        Log.e("LevelViewModel", "Parsed Level: $level")

                        _levelState.value = level
                    } else {
                        Log.e("LevelViewModel", "Server Error: ${connection.responseCode}")
                        _levelState.value = "low"
                    }
                } catch (e: Exception) {
                    Log.e("LevelViewModel", "Error reading response", e)
                    _levelState.value = "low"
                } finally {
                    connection.disconnect()
                }
            } catch (e: Exception) {
                Log.e("LevelViewModel", "Network Connection Error", e)
                _levelState.value = "low"
            }
        }
    }

}