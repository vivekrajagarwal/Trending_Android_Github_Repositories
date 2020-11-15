package com.popular.android.repos.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.popular.android.repos.R
import com.popular.android.repos.models.Item
import com.popular.android.repos.util.StoreItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class RepositoryDetail : AppCompatActivity() {
    var repos : Item? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        repos = StoreItem.viewedRepo
        if(repos != null){
            title = repos?.name
            viewActions()
        }
        viewOnGithub()
    }

    private fun viewActions(){
        Picasso.get().load(repos?.owner?.avatarUrl).into(user_display_imageView)
        repo_title.text = repos?.name
        repo_fullName.text = repos?.fullName
        repo_full_description.text = repos?.description
        repo_stars.text = repos?.stargazersCount.toString()
        repo_watchers.text = repos?.watchers.toString()
        repo_forks.text = repos?.forksCount.toString()
        repo_language.text = repos?.language
        repo_open_issues.text = repos?.openIssues.toString()
    }

    private fun viewOnGithub(){
        go_to_github.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repos?.htmlUrl))
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}
