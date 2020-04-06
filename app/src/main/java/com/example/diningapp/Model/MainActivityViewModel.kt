package com.example.diningapp.Model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diningapp.R
import com.example.diningapp.data.network.response.CurrentDiningResponse
import com.example.diningapp.data.network.response.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : ViewModel() {

    private var appRepositary = AppRepositary()
    lateinit var dinResponse: CurrentDiningResponse
    var venueList: MutableLiveData<ArrayList<Venue>> = MutableLiveData()
    var failure = MutableLiveData<String>()

    var venueDataList = ArrayList<Venue>()

    fun getAPIData(v: String, ll: String, context: Context) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    dinResponse = appRepositary.getData(v, ll, context)
                }
                venueDataList = dinResponse.response.venues
                venueList.value = venueDataList
            } catch (e: Exception) {
                failure.value = context.getString(R.string.no_restaurant);
            }
        }
    }
}