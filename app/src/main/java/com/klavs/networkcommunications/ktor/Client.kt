package com.klavs.networkcommunications.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Client {
    companion object {
        fun getKtorClient(): HttpClient {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
                install(WebSockets) {
                    pingIntervalMillis = 20_000
                }
            }
            return client
        }
    }
}