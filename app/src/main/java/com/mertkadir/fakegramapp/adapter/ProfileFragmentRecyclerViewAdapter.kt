package com.mertkadir.fakegramapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mertkadir.fakegramapp.R
import com.mertkadir.fakegramapp.databinding.RecyclerRowBinding
import com.mertkadir.fakegramapp.model.Posts
import com.mertkadir.fakegramapp.model.UserDetails


class ProfileFragmentRecyclerViewAdapter(val postList : ArrayList<Posts>) : RecyclerView.Adapter<ProfileFragmentRecyclerViewAdapter.PostsHolder>() {
    class PostsHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostsHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsHolder, position: Int) {

        holder.binding.commentTextView.text = postList.get(position).comment
        holder.binding.userEmailTextView.text = postList.get(position).email
        Glide.with(holder.binding.root).load(postList.get(position).downloadUrl).into(holder.binding.imageView)

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}