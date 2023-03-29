package com.dhandev.myapp1.ui.people

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhandev.myapp1.BuildConfig
import com.dhandev.myapp1.data.source.remote.network.ApiConfig
import com.dhandev.myapp1.data.source.remote.response.PeopleResponse
import com.dhandev.myapp1.data.source.remote.response.ResultsPeopleItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleViewModel: ViewModel() {
    private val _peopleData = MutableLiveData<List<ResultsPeopleItem>>()
    val peopleData : LiveData<List<ResultsPeopleItem>>
        get() = _peopleData

    fun getData(onFailedCallback: (String) -> Unit){
        ApiConfig.getApiService()
            .getPeople(BuildConfig.API_KEY, "en-US", 1)
            .enqueue(object : Callback<PeopleResponse> {
                override fun onResponse(
                    call: Call<PeopleResponse>,
                    response: Response<PeopleResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val peopleData = response.body()!!.results!!
                        _peopleData.postValue(peopleData)
                    } else {
                        Log.e("TAG", "onFailure: ${response.message()}")
                        onFailedCallback(response.message())
                    }
                }
                override fun onFailure(call: Call<PeopleResponse>, t: Throwable) {
                    Log.d("Failure", t.message!!)
                    onFailedCallback(t.message!!)
                }
            })
    }

}