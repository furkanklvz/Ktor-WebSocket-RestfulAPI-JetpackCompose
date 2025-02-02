package com.klavs.networkcommunications.data.datasource.restful

import com.klavs.networkcommunications.data.entity.ResponseObject
import com.klavs.networkcommunications.data.resource.Resource
import com.klavs.networkcommunications.ktor.RestfulService
import io.ktor.client.call.body
import io.ktor.http.isSuccess

class RestfulDatasourceImpl(private val ktorRestfulService: RestfulService) : RestfulDatasource {
    override suspend fun get(): Resource<ResponseObject> {
        return try {
            val response = ktorRestfulService.get()
            if (response.status.isSuccess()) {
                runCatching { Resource.Success(data = response.body<ResponseObject>()) }.getOrElse { e ->
                    Resource.Error(message = "Parsing error: ${e.message}")
                }
            } else {
                val errorMessage = runCatching { response.body<String>() }.getOrNull()
                Resource.Error(message = errorMessage ?: response.status.description)
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "unknown error")
        }
    }

    override suspend fun post(data: ResponseObject): Resource<ResponseObject> {
        return try {
            val response = ktorRestfulService.post(data)
            if (response.status.isSuccess()) {
                runCatching { Resource.Success(data = response.body<ResponseObject>()) }.getOrElse { e ->
                    Resource.Error(message = "Parsing error: ${e.message}")
                }
            } else {
                val errorMessage = runCatching { response.body<String>() }.getOrNull()
                Resource.Error(message = errorMessage ?: response.status.description)
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "unknown error")
        }
    }

    override suspend fun delete(): Resource<String> {
        return try {
            val response = ktorRestfulService.delete()
            if (response.status.isSuccess()) {
                Resource.Success(data = runCatching {
                    response.body<Any?>().toString()
                }.getOrElse { "Success" })
            } else {
                val errorMessage = runCatching { response.body<String>() }.getOrNull()
                Resource.Error(message = errorMessage ?: response.status.description)
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "unknown error")
        }
    }

    override suspend fun put(data: ResponseObject): Resource<ResponseObject> {
        return try {
            val response = ktorRestfulService.put(data)
            if (response.status.isSuccess()) {
                runCatching { Resource.Success(data = response.body<ResponseObject>()) }.getOrElse {
                    Resource.Error(message = "Parsing error: ${it.message}")
                }
            } else {
                runCatching { Resource.Error(message = response.body<String>()) }.getOrElse {
                    Resource.Error(
                        message = response.status.description
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "unknown error")
        }
    }

    override suspend fun patch(title: String): Resource<ResponseObject> {
        return try {
            val response = ktorRestfulService.patch(title)
            if (response.status.isSuccess()) {
                runCatching { Resource.Success(data = response.body<ResponseObject>()) }.getOrElse {
                    Resource.Error(
                        message = "parsing error: ${it.message}"
                    )
                }
            } else {
                runCatching { Resource.Error(message = response.body<String>()) }.getOrElse {
                    Resource.Error(
                        message = response.status.description
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "unknown error")
        }
    }

    override suspend fun filter(userId: Int): Resource<String> {
        return try {
            val response = ktorRestfulService.filter(userId)
            if (response.status.isSuccess()) {
                runCatching { Resource.Success(data = response.body<Any>().toString()) }.getOrElse {
                    Resource.Error(
                        message = "parsing error: ${it.message}"
                    )
                }
            } else {
                runCatching { Resource.Error(message = response.body<String>()) }.getOrElse {
                    Resource.Error(
                        message = response.status.description
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "unknown error")
        }
    }
}