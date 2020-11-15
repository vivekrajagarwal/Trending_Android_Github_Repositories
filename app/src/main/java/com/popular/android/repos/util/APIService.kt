package com.popular.android.repos.util

import com.popular.android.repos.models.RepoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("search/repositories?q=android%20language:java&sort=stars&order=desc&per_page=10")
    fun getRepositories(@Query("page") page: Int): Call<RepoModel>
}