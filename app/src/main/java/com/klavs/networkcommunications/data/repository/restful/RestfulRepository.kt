package com.klavs.networkcommunications.data.repository.restful

import com.klavs.networkcommunications.data.entity.ResponseObject
import com.klavs.networkcommunications.data.resource.Resource

interface RestfulRepository {
    suspend fun get(): Resource<ResponseObject>
    suspend fun post(data: ResponseObject): Resource<ResponseObject>
    suspend fun delete(): Resource<String>
    suspend fun put(data: ResponseObject): Resource<ResponseObject>
    suspend fun patch(title:String): Resource<ResponseObject>
    suspend fun filter(userId:Int): Resource<String>
}