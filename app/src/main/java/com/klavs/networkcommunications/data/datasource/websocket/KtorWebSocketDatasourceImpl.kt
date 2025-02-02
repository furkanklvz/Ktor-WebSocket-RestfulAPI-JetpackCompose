package com.klavs.networkcommunications.data.datasource.websocket

import com.klavs.networkcommunications.ktor.WebSocketService

class KtorWebSocketDatasourceImpl(private val webSocketService: WebSocketService) : KtorWebSocketDatasource {
    override suspend fun initSession() =webSocketService.initSession()
    override suspend fun sendMessage(message: String) = webSocketService.sendMessage(message)
    override fun observeMessages() = webSocketService.observeMessages()
    override suspend fun closeSession() = webSocketService.closeSession()
}