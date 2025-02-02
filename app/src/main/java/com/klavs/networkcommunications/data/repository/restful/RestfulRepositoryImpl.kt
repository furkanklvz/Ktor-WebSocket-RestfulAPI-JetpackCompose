package com.klavs.networkcommunications.data.repository.restful

import com.klavs.networkcommunications.data.datasource.restful.RestfulDatasource
import com.klavs.networkcommunications.data.entity.ResponseObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestfulRepositoryImpl(private val ds: RestfulDatasource) : RestfulRepository {
    override suspend fun get() = withContext(Dispatchers.IO){ds.get()}
    override suspend fun post(data: ResponseObject) = withContext(Dispatchers.IO){ds.post(data)}
    override suspend fun delete() = withContext(Dispatchers.IO){ds.delete()}
    override suspend fun put(data: ResponseObject) = withContext(Dispatchers.IO){ds.put(data)}
    override suspend fun patch(title: String) = withContext(Dispatchers.IO){ds.patch(title)}
    override suspend fun filter(userId: Int) = withContext(Dispatchers.IO){ds.filter(userId)}
}