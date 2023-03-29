package com.dhandev.myapp1.ui.detail.people

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.remote.response.KnownForItem
import com.dhandev.myapp1.data.source.remote.response.ResultsItem
import com.dhandev.myapp1.data.source.remote.response.ResultsPeopleItem
import com.dhandev.myapp1.databinding.ActivityPeopleDetailBinding
import com.dhandev.myapp1.ui.detail.DetailActivity
import com.dhandev.myapp1.utils.UiUtils
import com.dhandev.myapp1.utils.dateUtil
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton

class PeopleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeopleDetailBinding
    private lateinit var skeleton: Skeleton
    private lateinit var skeletonBio: Skeleton
    private lateinit var skeletonRv: Skeleton
    private lateinit var adapter: PeopleMovieListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel : PeopleDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleDetailBinding.inflate(layoutInflater)

        // Calling the support action bar and setting it to custom
        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        // Displaying the custom layout in the ActionBar
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvBack = findViewById<TextView>(R.id.tvBack)

        val data = intent.getParcelableExtra<ResultsPeopleItem>(DETAIL_INFO)
        tvTitle.text = data?.name
        tvBack.text = getString(R.string.popular_people)
        tvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setContentView(binding.root)

        adapter = PeopleMovieListAdapter()
        binding.rvKnownFor.adapter = adapter

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvKnownFor.layoutManager = linearLayoutManager

        adapter.delegate = object : PeopleMovieDelegate {
            override fun onItemClicked(selected: KnownForItem) {
                val title = selected.originalTitle ?: selected.originalName.toString()
                val release = selected.releaseDate ?: selected.firstAirDate.toString()
                val dataResult = ResultsItem(selected.overview, title, selected.title, release, selected.posterPath, selected.backdropPath, selected.voteAverage, selected.id)
                DetailActivity.open(this@PeopleDetailActivity, "Movie Detail", data?.name!!, dataResult)
            }
        }
        adapter.setAdapter(data?.knownFor!!)

        binding.rvKnownFor.isVisible = data.knownFor.isNotEmpty()

        skeleton = binding.mainInfo.createSkeleton()
        skeletonBio = binding.tvBio.createSkeleton()
        skeletonRv = binding.rvKnownFor.applySkeleton(R.layout.list_row_people_item, 5)

        getData()

    }

    private fun getData() {
        val data = intent.getParcelableExtra<ResultsPeopleItem>(DETAIL_INFO)

        skeleton.showSkeleton()
        skeletonBio.showSkeleton()
        val loading = UiUtils().showLoading(this)

        viewModel.getData(data?.id!!){
            showAlert(it)
        }
        viewModel.peopleDetailData.observe(this){peopleData->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original/${peopleData.profilePath}")
                .into(binding.ivProfile)
            binding.tvName.text = peopleData.name
            val gender = if(peopleData.gender == 2) "Male" else "Female"
            binding.tvAct.text = "$gender, ${peopleData.knownForDepartment}"

            val birthDate = peopleData.birthday?.split("-")
            val year = birthDate?.get(0)
            val month = birthDate?.get(1)
            val day = birthDate?.get(2)
            val deathDate = peopleData.deathday?.split("-")
            val yearDeath = deathDate?.get(0)
            val monthDeath = deathDate?.get(1)
            val dayDeath = deathDate?.get(2)
            if (birthDate != null && peopleData.deathday == null) {
                binding.tvBirthday.text = "${peopleData.birthday} (${dateUtil().getDate(year!!, month!!, day!!)} years old)"
            } else if (peopleData.deathday != null){
                binding.tvBirthday.text = "${peopleData.birthday} (Death at ${dateUtil().getDeathDate(year!!, month!!, day!!, yearDeath!!, monthDeath!!, dayDeath!!)} years old)"
            }
            binding.tvLoc.text = peopleData.placeOfBirth
            binding.tvKnownAs.text = peopleData.alsoKnownAs?.joinToString(",")
            binding.tvBio.text = peopleData.biography
            skeleton.showOriginal()
            skeletonBio.showOriginal()
            loading.dismiss()
        }
    }

    private fun showAlert(message: String){
        //show alert by creating showAlert instance from UiUtils, pass needed parameters, and add callback
        //for positive and negative button
        UiUtils().showAlert(this, "Warning", message, "Retry", "Back",
            {
                getData()
            }, {
                onBackPressedDispatcher.onBackPressed()
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
        const val PAGE_TITLE = "page_title"
        const val DETAIL_INFO = "detail_info"

        fun open(activity: AppCompatActivity, title: String, data: ResultsPeopleItem){
            val intent = Intent(activity, PeopleDetailActivity::class.java)
            intent.putExtra(PAGE_TITLE, title)
            intent.putExtra(DETAIL_INFO, data)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}