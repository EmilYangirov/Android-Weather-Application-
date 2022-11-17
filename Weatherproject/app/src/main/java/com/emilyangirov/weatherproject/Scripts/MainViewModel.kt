package com.emilyangirov.weatherproject.Scripts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val cityData = MutableLiveData<CityData>()
}