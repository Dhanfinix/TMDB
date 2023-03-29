package com.dhandev.myapp1.ui.comment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dhandev.myapp1.R
import com.dhandev.myapp1.data.source.local.entity.CommentEntity
import com.dhandev.myapp1.databinding.ActivityCommentBinding

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding
    private val viewModel: CommentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setHomeButtonEnabled(true)
        title = getString(R.string.comment)

        val movieTvId = intent.getIntExtra(MOVIETV_ID, 0)
        val commentData = intent.getParcelableExtra<CommentEntity>(COMMENT_INFO)
        // TODO: UPDATE DELETE COMMENT

        binding.apply {
            etNameComment.setText(commentData?.username)
            etBodyComment.setText(commentData?.commentBody)


            var name: String
            var comment: String
            if (commentData != null) {
                viewModel.getCommentById(
                    this@CommentActivity,
                    this@CommentActivity,
                    commentData.id
                )
                btnDelete.visibility = View.VISIBLE
                viewModel.commentById.observe(this@CommentActivity) { commentEntity ->
                    btnComment.setOnClickListener {
                        name = etNameComment.text.toString()
                        comment = etBodyComment.text.toString()
                        println("THIS IS COMMENT ENITITY -> $commentEntity")
                        if (validate(name, comment)) {
                            viewModel.updateComment(
                                this@CommentActivity,
                                CommentEntity(
                                    username = name,
                                    commentBody = comment,
                                    movieId = commentEntity.movieId,
                                    id = commentEntity.id
                                )
                            )
                            finish()
                        }
                    }
                    btnDelete.setOnClickListener {
                        viewModel.deleteComment(this@CommentActivity, commentEntity)
                        finish()
                    }
                }
            } else {

                btnDelete.visibility = View.GONE

                btnComment.setOnClickListener {
                    name = etNameComment.text.toString()
                    comment = etBodyComment.text.toString()
                    if (validate(name, comment)) {
                        viewModel.insertComment(
                            this@CommentActivity,
                            CommentEntity(
                                username = name,
                                commentBody = comment,
                                movieId = movieTvId
                            )
                        )
                        finish()
                    }
                }
            }


        }
    }

    private fun validate(name: String, comment: String): Boolean {
        val result: Boolean = if (name.isEmpty()) {
            Toast.makeText(this, "Please fill name input", Toast.LENGTH_SHORT).show()
            false
        } else if (comment.isEmpty()) {
            Toast.makeText(this, "Please fill comment input", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
        return result
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return true

    }

    companion object {
        const val COMMENT_INFO = "comment_info"
        const val MOVIETV_ID = "movietv_id"

        fun openNew(activity: AppCompatActivity, movieTvId: Int) {
            val intent = Intent(activity, CommentActivity::class.java)
            intent.putExtra(MOVIETV_ID, movieTvId)
            ActivityCompat.startActivity(activity, intent, null)
        }

        fun openToUpdate(activity: AppCompatActivity, data: CommentEntity) {
            val intent = Intent(activity, CommentActivity::class.java)
            intent.putExtra(COMMENT_INFO, data)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}