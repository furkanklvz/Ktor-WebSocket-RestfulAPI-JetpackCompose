package com.klavs.networkcommunications.uix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.networkcommunications.data.entity.ResponseObject
import com.klavs.networkcommunications.data.repository.restful.RestfulRepository
import com.klavs.networkcommunications.data.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RestfulViewModel(private val repo: RestfulRepository): ViewModel() {

    private val _getResource = MutableStateFlow<Resource<ResponseObject>>(Resource.Idle)
    val getResource = _getResource.asStateFlow()
    private val _postResource = MutableStateFlow<Resource<ResponseObject>>(Resource.Idle)
    val postResource = _postResource.asStateFlow()
    private val _deleteResource = MutableStateFlow<Resource<String>>(Resource.Idle)
    val deleteResource = _deleteResource.asStateFlow()
    private val _putResource = MutableStateFlow<Resource<ResponseObject>>(Resource.Idle)
    val putResource = _putResource.asStateFlow()
    private val _patchResource = MutableStateFlow<Resource<ResponseObject>>(Resource.Idle)
    val patchResource = _patchResource.asStateFlow()
    private val _filterResource = MutableStateFlow<Resource<String>>(Resource.Idle)
    val filterResource = _filterResource.asStateFlow()

    fun get(){
        _getResource.value = Resource.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _getResource.value = repo.get()
        }
    }

    fun post(data: ResponseObject){
        _postResource.value = Resource.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _postResource.value = repo.post(data)
        }
    }
    fun delete(){
        _deleteResource.value = Resource.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _deleteResource.value = repo.delete()
        }
    }
    fun put(data: ResponseObject){
        _putResource.value = Resource.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _putResource.value = repo.put(data)
        }
    }
    fun patch(title: String) {
        _patchResource.value = Resource.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _patchResource.value = repo.patch(title)
        }
    }
    fun filter(userId: Int) {
        _filterResource.value = Resource.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _filterResource.value = repo.filter(userId)
        }
    }

}