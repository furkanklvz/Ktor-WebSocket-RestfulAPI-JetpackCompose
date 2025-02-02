package com.klavs.networkcommunications.di

import com.klavs.networkcommunications.data.datasource.restful.RestfulDatasource
import com.klavs.networkcommunications.data.datasource.restful.RestfulDatasourceImpl
import com.klavs.networkcommunications.data.datasource.websocket.KtorWebSocketDatasource
import com.klavs.networkcommunications.data.datasource.websocket.KtorWebSocketDatasourceImpl
import com.klavs.networkcommunications.data.repository.restful.RestfulRepository
import com.klavs.networkcommunications.data.repository.restful.RestfulRepositoryImpl
import com.klavs.networkcommunications.data.repository.websocket.KtorWebSocketRepository
import com.klavs.networkcommunications.data.repository.websocket.KtorWebSocketRepositoryImpl
import com.klavs.networkcommunications.ktor.Client
import com.klavs.networkcommunications.ktor.RestfulService
import com.klavs.networkcommunications.ktor.WebSocketService
import com.klavs.networkcommunications.uix.viewmodel.RestfulViewModel
import com.klavs.networkcommunications.uix.viewmodel.WebSocketViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { Client.getKtorClient() }
    singleOf (::RestfulService)
    singleOf(::WebSocketService)
    singleOf(::RestfulDatasourceImpl) { bind<RestfulDatasource>() }
    singleOf(::RestfulRepositoryImpl) { bind<RestfulRepository>()}
    singleOf(::KtorWebSocketDatasourceImpl) {bind<KtorWebSocketDatasource>()}
    singleOf(::KtorWebSocketRepositoryImpl) {bind<KtorWebSocketRepository>()}
    viewModelOf(::RestfulViewModel)
    viewModelOf(::WebSocketViewModel)
}