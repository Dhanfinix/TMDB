package com.dhandev.myapp1.ui.people

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dhandev.myapp1.data.source.remote.response.ResultsPeopleItem
import com.dhandev.myapp1.databinding.ActivityPeopleDetailBinding
import com.dhandev.myapp1.ui.detail.DetailActivity

class PeopleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeopleDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = intent.getStringExtra(PAGE_TITLE)
        val data = intent.getStringExtra(DETAIL_INFO)

        binding.apply {

        }
    }

    companion object{
        const val PAGE_TITLE = "page_title"
        const val DETAIL_INFO = "detail_info"

        fun open(activity: AppCompatActivity, title: String, data: ResultsPeopleItem){
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(DETAIL_INFO, data)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}