package com.klavs.networkcommunications.data.repository.websocket

import com.klavs.networkcommunications.data.datasource.websocket.KtorWebSocketDatasource
import com.klavs.networkcommunications.data.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class KtorWebSocketRepositoryImpl(private val ds: KtorWebSocketDatasource) : KtorWebSocketRepository {
    override suspend fun initSession() = withContext(Dispatchers.IO){ds.initSession()}
    override suspend fun sendMessage(message: String) = withContext(Dispatchers.IO){ds.sendMessage(message)}
    override fun observeMessages() = ds.observeMessages().flowOn(Dispatchers.IO)
    override suspend fun closeSession() = withContext(Dispatchers.IO){ds.closeSession()}
}