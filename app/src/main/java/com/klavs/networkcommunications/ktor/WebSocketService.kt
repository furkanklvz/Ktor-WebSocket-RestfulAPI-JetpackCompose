package com.klavs.networkcommunications.ktor

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.klavs.networkcommunications.data.resource.Resource
import com.klavs.networkcommunications.uix.view.MessageType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.host
import io.ktor.client.request.port
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive

class WebSocketService(private val client: HttpClient) {

    private var socket: WebSocketSession? = null

    suspend fun initSession(): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                method = HttpMethod.Get
                host = "echo.websocket.org"
                port = 80
            }
            if (socket?.isActive == true) {
                Resource.Success(data = Unit)
            } else {
                Resource.Error(message = "Could not be connected")
            }
        } catch (e: Exception) {
            Log.e("WebSocketService", e.toString())
            Resource.Error(message = e.localizedMessage ?: e.toString())
        }
    }

    suspend fun sendMessage(message: String) {
        runCatching { socket?.send(message) }.exceptionOrNull()
            ?.let { Log.e("WebSocketService", it.message.toString()) }
    }

    fun observeMessages(): Flow<Resource<String>> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map { frame ->
                    val message = (frame as Frame.Text).readText()
                    Resource.Success(data = message)
                }
                ?: flow { emit(Resource.Error(message = "Session not active")) }
        } catch (e: Exception) {
            Log.e("WebSocketService", e.toString())
            flow { emit(Resource.Error(message = "Error: ${e.message}")) }
        }
    }

    suspend fun closeSession() {
        socket?.close()
    }
}