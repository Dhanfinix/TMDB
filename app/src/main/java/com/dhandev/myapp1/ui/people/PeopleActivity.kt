package com.dhandev.myapp1.ui.people

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.remote.response.ResultsPeopleItem
import com.dhandev.myapp1.databinding.ActivityPeopleBinding
import com.dhandev.myapp1.ui.detail.people.PeopleDetailActivity
import com.dhandev.myapp1.ui.list.ListActivity
import com.dhandev.myapp1.utils.UiUtils
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton

class PeopleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeopleBinding
    private lateinit var adapter: PeopleListAdapter
    private lateinit var skeleton: Skeleton
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel : PeopleViewModel by viewModels()
    private lateinit var loading : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleBinding.inflate(layoutInflater)

        // Calling the support action bar and setting it to custom
        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        // Displaying the custom layout in the ActionBar
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvBack = findViewById<TextView>(R.id.tvBack)

        tvTitle.text = intent.getStringExtra(ListActivity.LIST_TITLE)
        tvBack.text = getString(R.string.home)
        tvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setContentView(binding.root)

        adapter = PeopleListAdapter()
        binding.rvList.adapter = adapter

        linearLayoutManager = GridLayoutManager(this, 3)
        binding.rvList.layoutManager = linearLayoutManager

        adapter.delegate = object : PeopleDelegate {
            override fun onItemClicked(selected: ResultsPeopleItem) {
                PeopleDetailActivity.open(this@PeopleActivity, "Detail", selected)
            }
        }
        skeleton = binding.rvList.applySkeleton(R.layout.list_row_people_item, 9)

        //get data from API
        getData()

        binding.swipeToRefresh.setOnRefreshListener {
            //disable inherited loading of swipe layout
            binding.swipeToRefresh.isRefreshing = false
            //get data from API
            getData()
        }
    }

    private fun getData() {
        //show shimmering/skeleton and loading popup
        skeleton.showSkeleton()
        loading = UiUtils().showLoading(this)

        //get data by calling it from view model, pass path(endpoint), query(for search),
        // and add callback for on response and error
        viewModel.getData { errorMsg ->
            showAlert(errorMsg)
        }
        viewModel.peopleData.observe(this){peopleData->
            adapter.setAdapter(peopleData)
            binding.rvList.isVisible = peopleData.isNotEmpty()
            skeleton.showOriginal()
            loading.dismiss()
        }
    }

    private fun showAlert(message: String) {
        //show alert by creating showAlert instance from UiUtils, pass needed parameters, and add callback
        //for positive and negative button
        UiUtils().showAlert(this, "Warning", message, "Retry", "Back",
            {
                getData()
            }, {
                onBackPressedDispatcher.onBackPressed()
            })
    }

    //overide back button on action bar with onBackPressed, because the default swipe to opposite direction
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