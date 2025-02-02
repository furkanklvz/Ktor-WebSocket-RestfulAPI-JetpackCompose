package com.klavs.networkcommunications.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class ResponseObject(
    val id: Int = 0,
    val body: String,
    val title: String,
    val userId: Int
)