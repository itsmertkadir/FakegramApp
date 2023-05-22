package com.mertkadir.fakegramapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mertkadir.fakegramapp.databinding.RecyclerRowBinding
import com.mertkadir.fakegramapp.model.Posts


class HomeFragmentRecyclerViewAdapter(private val postList : ArrayList<Posts>) : RecyclerView.Adapter<HomeFragmentRecyclerViewAdapter.PostsHolder>() {

    private lateinit var db : FirebaseFirestore
    class PostsHolder(val binding : RecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        db = Firebase.firestore

        return PostsHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsHolder, position: Int) {

        db.collection("UserInfo").whereEqualTo("userUUID", postList.get(position).userPostId).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(holder.itemView.context, error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if (value != null){

                    if (!value.isEmpty){

                        val documents = value.documents


                        val image = documents[0].getString("userProfileImage") as String
                        val userName = documents[0].getString("userName") as String

                        if (userName == null){
                            holder.binding.userEmailTextView.text = postList.get(position).email
                        }else {
                            holder.binding.userEmailTextView.text = userName
                        }


                        if (image == null){

                            Toast.makeText(holder.itemView.context, "pp null", Toast.LENGTH_SHORT).show()

                        }else{
                            Glide.with(holder.binding.root).load(image).into(holder.binding.postUserImage)
                        }



                    }

                }
            }
        }




        //holder.binding.userEmailTextView.text = postList.get(position).email
        holder.binding.commentTextView.text = postList.get(position).comment
        Glide.with(holder.binding.root).load(postList.get(position).downloadUrl).into(holder.binding.imageView)


    }

    override fun getItemCount(): Int {
        return postList.size
    }
}