package com.popular.android.repos.util

object GithubAPIUtils {

    val BASE_URL = "https://api.github.com/"

    val apiService: APIService
        get() = RetrofitClient.getClient(BASE_URL).create(APIService::class.java)
}