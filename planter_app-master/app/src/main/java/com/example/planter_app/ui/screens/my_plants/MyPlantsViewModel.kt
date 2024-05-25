package com.example.planter_app.ui.screens.my_plants

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyPlantsViewModel:ViewModel() {

    companion object{
        var triggerPlantDeleteBottomSheet = mutableStateOf(false)
        var displayPlantDeleteBottomSheet = mutableStateOf(false)
    }


    private val _isImageExpanded = MutableStateFlow(false)
    val isImageExpanded : StateFlow<Boolean> = _isImageExpanded.asStateFlow()

    fun setIsImageExpanded(boolean: Boolean){
        _isImageExpanded.value = boolean
    }


    private val _plantNetApiResponse = MutableStateFlow<String?>(null)
    val plantNetApiResponse: StateFlow<String?> = _plantNetApiResponse.asStateFlow()

    fun setPlantNetApiResponse(string: String){
        _plantNetApiResponse.value = string
    }

    private val _mlModelApiResponseResult = MutableStateFlow<String?>(null)
    val mlModelApiResponseResult: StateFlow<String?> = _mlModelApiResponseResult.asStateFlow()

    fun setMlModelApiResponseResult(string: String){
        _mlModelApiResponseResult.value = string
    }

    private val _mlModelApiResponseAdvice = MutableStateFlow<String?>(null)
    val mlModelApiResponseAdvice: StateFlow<String?> = _mlModelApiResponseAdvice.asStateFlow()

    fun setMlModelApiResponseAdvice(string: String){
        _mlModelApiResponseAdvice.value = string
    }

    private val _mlModelApiResponseError = MutableStateFlow<String?>(null)
    val mlModelApiResponseError: StateFlow<String?> = _mlModelApiResponseError.asStateFlow()

    fun setMlModelApiResponseError(string: String){
        _mlModelApiResponseError.value = string
    }

}