package com.dhandev.myapp1.ui.detail.people

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhandev.myapp1.BuildConfig
import com.dhandev.myapp1.data.source.remote.network.ApiConfig
import com.dhandev.myapp1.data.source.remote.response.PeopleDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleDetailViewModel: ViewModel() {

    private val _peopleDetailData = MutableLiveData<PeopleDetailResponse>()
    val peopleDetailData : LiveData<PeopleDetailResponse>
        get() = _peopleDetailData

    fun getData(id: Int, onFailedCallback: (String) -> Unit){

        ApiConfig.getApiService()
            .getPeopleDetail(id, BuildConfig.API_KEY, "en-US", 1)
            .enqueue(object : Callback<PeopleDetailResponse> {
                override fun onResponse(
                    call: Call<PeopleDetailResponse>,
                    response: Response<PeopleDetailResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val peopleData = response.body()!!
                        _peopleDetailData.postValue(peopleData)
                    } else {
                        Log.e("TAG", "onFailure: ${response.message()}")
                        onFailedCallback(response.message())
                    }
                }
                override fun onFailure(call: Call<PeopleDetailResponse>, t: Throwable) {
                    onFailedCallback(t.message!!)
                    Log.d("Failure", t.message!!)
                }
            })
    }

}