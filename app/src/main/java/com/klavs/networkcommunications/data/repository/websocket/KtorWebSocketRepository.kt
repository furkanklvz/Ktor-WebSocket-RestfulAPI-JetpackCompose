package com.klavs.networkcommunications.data.repository.websocket

import com.klavs.networkcommunications.data.resource.Resource
import kotlinx.coroutines.flow.Flow

interface KtorWebSocketRepository {
    suspend fun initSession(): Resource<Unit>
    suspend fun sendMessage(message:String)
    fun observeMessages(): Flow<Resource<String>>
    suspend fun closeSession()
}