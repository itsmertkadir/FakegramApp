package com.mertkadir.fakegramapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mertkadir.fakegramapp.adapter.HomeFragmentRecyclerViewAdapter
import com.mertkadir.fakegramapp.databinding.FragmentHomeBinding
import com.mertkadir.fakegramapp.model.Posts

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Posts>
    private lateinit var homeFragmentAdapter : HomeFragmentRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore
        postArrayList = ArrayList<Posts>()

        getData()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(this@HomeFragment.context)
        homeFragmentAdapter = HomeFragmentRecyclerViewAdapter(postArrayList)
        binding.recyclerView.adapter = homeFragmentAdapter

        binding.addPage.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddPostFragment2()
            Navigation.findNavController(it).navigate(action)
        }

        binding.profilePage.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            Navigation.findNavController(it).navigate(action)
        }


    }

    private fun getData(){
        db.collection("Posts").addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(this.context, error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if (value != null){

                    if (!value.isEmpty){

                        val documents = value.documents

                        for (document in documents){

                            val comment = document.get("comment") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl")as String
                            val posts = Posts(userEmail,comment,downloadUrl)
                            postArrayList.add(posts)
                        }

                        homeFragmentAdapter.notifyDataSetChanged()

                    }

                }
            }

        }
    }

}