package com.klavs.networkcommunications.uix.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.networkcommunications.data.repository.websocket.KtorWebSocketRepository
import com.klavs.networkcommunications.data.resource.Resource
import com.klavs.networkcommunications.uix.view.MessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WebSocketViewModel(private val repo: KtorWebSocketRepository): ViewModel() {

    private val _initSessionResourceFlow = MutableStateFlow<Resource<Unit>>(Resource.Loading)
    val initSessionResourceFlow = _initSessionResourceFlow.asStateFlow()

    private val _messagesResourceFlow = MutableStateFlow<Resource<String>>(Resource.Idle)
    val messagesResourceFlow = _messagesResourceFlow.asStateFlow()

    init {
        initSession()
    }

    fun clearMessages(){
        _messagesResourceFlow.value = Resource.Idle
    }

    private fun initSession(){
        viewModelScope.launch(Dispatchers.Main) {
            _initSessionResourceFlow.value = repo.initSession()
            if (_initSessionResourceFlow.value.isSuccess()){
                observeMessages()
            }
        }
    }

    private fun observeMessages(){
        viewModelScope.launch(Dispatchers.Main) {
            repo.observeMessages().collect{resource->
                _messagesResourceFlow.value = resource
            }
        }
    }
    fun sendMessage(message: String){
        viewModelScope.launch(Dispatchers.Main) {
            repo.sendMessage(message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch { repo.closeSession() }
    }
}