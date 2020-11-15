package com.popular.android.repos.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast
import com.popular.android.repos.R
import com.popular.android.repos.adapter.RepoRecyclerListAdapter
import com.popular.android.repos.models.Item
import com.popular.android.repos.models.RepoModel
import com.popular.android.repos.util.APIService
import com.popular.android.repos.util.GithubAPIUtils
import kotlinx.android.synthetic.main.activity_repos_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryList : AppCompatActivity() {
    var reposList : MutableList<Item> = arrayListOf()
    var apiService : APIService? = null
    lateinit var recyclerAdapter : RepoRecyclerListAdapter
    private var layoutManager : LinearLayoutManager? = null
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repos_list)
        setTitle(R.string.repo_list_activity_name)
        apiService = GithubAPIUtils.apiService
        getRepos()
        viewActions()
        networkChecker()
    }

    private fun viewActions(){
        // set up RecyclerView
        layoutManager = LinearLayoutManager(this)
        recyclerAdapter = RepoRecyclerListAdapter(reposList)
        repo_recyclerView.layoutManager = layoutManager
        repo_recyclerView.adapter = recyclerAdapter
        setRecyclerListenerForPagination()
    }

    private fun getRepos(){
        //makes a call to get trending repositories
        apiService?.getRepositories(page)?.enqueue(object : Callback<RepoModel>{
            override fun onFailure(call: Call<RepoModel>?, t: Throwable?) {
                // take action to show that repositories were not gotten
                onFailToGetRepos()
            }
            override fun onResponse(call: Call<RepoModel>?, response: Response<RepoModel>?) {
                if(response != null){
                    if(response.isSuccessful){
                        val repo = response.body()
                        reposList.addAll(repo?.items!!)
                        recyclerAdapter.repos = reposList
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun onFailToGetRepos(){
        val backgroundLayout = findViewById(R.id.repos_layout) as LinearLayout
        Snackbar.make(backgroundLayout, "Failed to get trending repos", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY") {
                    getRepos()
                    toastMethod("Fetching Trending Repos")
                }.setActionTextColor(resources.getColor(R.color.backgroundColor)).show()
    }

    private val lastItemShowingPosition : Int
        get() = layoutManager!!.findLastVisibleItemPosition()

    private fun setRecyclerListenerForPagination(){
        //setting up the recycler listener for endless Pagination
        repo_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalNumberOfItems = recyclerView.layoutManager?.itemCount
                if (totalNumberOfItems == lastItemShowingPosition + 1) {
                    //last item is now showing so increment page count and load more of the repos
                    page += 1
                    getRepos()
                }
            }
        })
    }

    private fun networkChecker(){
        val backgroundLayout = findViewById(R.id.repos_layout) as LinearLayout
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Snackbar.make(backgroundLayout, "Connection successful", Snackbar.LENGTH_SHORT).show()
        } else {
            //we are not connected to a network
            Snackbar.make(backgroundLayout, "Oops! No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY") {
                        val intent = intent
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }.setActionTextColor(resources.getColor(R.color.backgroundColor)).show()
        }
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
