package com.dhandev.myapp1.ui.people

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhandev.myapp1.BuildConfig
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.remote.network.ApiConfig
import com.dhandev.myapp1.data.source.remote.response.PeopleResponse
import com.dhandev.myapp1.databinding.ActivityPeopleBinding
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeopleBinding
    private lateinit var adapter: PeopleListAdapter
    private lateinit var skeleton: Skeleton
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setHomeButtonEnabled(true)

        title = intent.getStringExtra(LIST_TITLE)

        adapter = PeopleListAdapter()
        binding.rvList.adapter = adapter

        linearLayoutManager = GridLayoutManager(this, 3)
        binding.rvList.layoutManager = linearLayoutManager

//        adapter.delegate = object : PeopleDelegate {
//            override fun onItemClicked(selected: ResultsPeopleItem) {
//                DetailActivity.open(this@PeopleActivity, "Detail", selected)
//            }
//
//        }
        skeleton = binding.rvList.applySkeleton(R.layout.list_row_people_item, 9)

        skeleton.showSkeleton()
        ApiConfig.getApiService()
            .getPeople(BuildConfig.API_KEY, "en-US", 1)
            .enqueue(object : Callback<PeopleResponse> {
                override fun onResponse(
                    call: Call<PeopleResponse>,
                    response: Response<PeopleResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val peopleData = response.body()!!.results!!
                        adapter.setAdapter(peopleData)
                        binding.rvList.isVisible = peopleData.isNotEmpty()
                        skeleton.showOriginal()
                    } else {
                        Log.e("TAG", "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<PeopleResponse>, t: Throwable) {
                    Log.d("Failure", t.message!!)
                }
            })
    }


    //overide back button pada action bar dengan onBackPressed, karena defaultnya seperti merestart activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == android.R.id.home){
            onBackPressedDispatcher.onBackPressed()
        }
        return true

    }

    companion object{
        const val LIST_TITLE = "list_title"
        fun open(activity: AppCompatActivity, title: String){
            val intent = Intent(activity, PeopleActivity::class.java)
            intent.putExtra(LIST_TITLE, title)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}