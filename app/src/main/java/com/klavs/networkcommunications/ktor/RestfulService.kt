package com.klavs.networkcommunications.ktor

import com.klavs.networkcommunications.data.entity.ResponseObject
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.path

class RestfulService(private val client: HttpClient) {
    private val baseUrl = "jsonplaceholder.typicode.com"
    suspend fun get(): HttpResponse {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = baseUrl
                path("posts", "1")
            }
        }
        return response
    }

    suspend fun post(data: ResponseObject): HttpResponse {
        val response = client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = baseUrl
                path("posts")
            }
            setBody(data)
            header("Content-type", "application/json; charset=UTF-8")
        }
        return response
    }

    suspend fun delete(): HttpResponse {
        val response = client.delete {
            url {
                protocol = URLProtocol.HTTPS
                host = baseUrl
                path("posts", "1")
            }
        }
        return response
    }

    suspend fun put(data: ResponseObject): HttpResponse {
        val response = client.put {
            url {
                protocol = URLProtocol.HTTPS
                host = baseUrl
                path("posts", "1")
            }
            setBody(data)
            header("Content-type", "application/json; charset=UTF-8")
        }
        return response
    }

    suspend fun patch(title: String): HttpResponse {
        val response = client.patch {
            url {
                protocol = URLProtocol.HTTPS
                host = baseUrl
                path("posts", "1")
            }
            setBody(hashMapOf("title" to title))
            header("Content-type", "application/json; charset=UTF-8")
        }
        return response
    }

    suspend fun filter(userId: Int): HttpResponse {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = baseUrl
                parameter("userId", userId)
            }
        }
        return response
    }

}