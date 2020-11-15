package com.popular.android.repos.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popular.android.repos.R
import com.popular.android.repos.ui.RepositoryDetail
import com.popular.android.repos.models.Item
import com.popular.android.repos.util.StoreItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.repo_item.view.*

class RepoRecyclerListAdapter(repos : MutableList<Item>) : RecyclerView.Adapter<RepoRecyclerListAdapter.ViewHolder>(){
    var repos : MutableList<Item> = repos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoRecyclerListAdapter.ViewHolder{
        val inflatedLayout = LayoutInflater.from(parent.context).inflate(R.layout.repo_item, parent, false)
        return ViewHolder(inflatedLayout)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    override fun onBindViewHolder(holder: RepoRecyclerListAdapter.ViewHolder, position: Int) {
        val repo = repos[position]
        holder.bindRepos(repo)
    }

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v), View.OnClickListener{
        private var view : View = v
        private var repo : Item? = null
        init{
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = itemView.context
            val intent = Intent(context, RepositoryDetail::class.java)
            StoreItem.viewedRepo = repo
            context.startActivity(intent)
        }

        fun bindRepos(repo : Item){
            this.repo = repo
            Picasso.get().load(repo.owner?.avatarUrl)
                    .placeholder(R.color.backgroundColor)
                    .error(android.R.drawable.stat_notify_error)
                    .tag(itemView.context)
                    .into(view.user_dp)
            
            view.repo_title_textView.text = repo.name
            view.repo_description_textView.text = repo.description
            view.repo_watchers_textView.text = repo.forksCount.toString()
            view.repo_language.text = repo.language
        }
    }

}