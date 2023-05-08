package com.mertkadir.fakegramapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mertkadir.fakegramapp.adapter.ProfileFragmentRecyclerViewAdapter
import com.mertkadir.fakegramapp.databinding.FragmentProfileBinding
import com.mertkadir.fakegramapp.model.Posts
import com.mertkadir.fakegramapp.model.UserDetails



class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Posts>
    private lateinit var profileFragmentAdapter : ProfileFragmentRecyclerViewAdapter
    private lateinit var currentUserEmail: String
    private lateinit var userDetailsArray: ArrayList<UserDetails>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore
        postArrayList = ArrayList<Posts>()
        userDetailsArray = ArrayList<UserDetails>()
            currentUserEmail = auth.currentUser!!.email.toString()

        getData()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        profileFragmentAdapter = ProfileFragmentRecyclerViewAdapter(postArrayList)
        binding.recyclerView.adapter = profileFragmentAdapter


        binding.addPage.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToAddPostFragment2()
            Navigation.findNavController(it).navigate(action)
        }

        binding.homePage.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToHomeFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.settingView.setOnClickListener {

            val action = ProfileFragmentDirections.actionProfileFragmentToProfileSettingsFragment()
            Navigation.findNavController(it).navigate(action)
            
        }

    }

    private fun getData(){
        db.collection("Posts").whereEqualTo("userEmail",auth.currentUser!!.email).orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(this.context, error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if (value != null){

                    if (!value.isEmpty){

                        val documents = value.documents

                        postArrayList.clear()

                        for (document in documents){

                            val comment = document.get("comment") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl")as String
                            val posts = Posts(userEmail,comment,downloadUrl)
                            postArrayList.add(posts)
                        }

                        profileFragmentAdapter.notifyDataSetChanged()

                    }

                }
            }

        }

        db.collection("UserInfo").whereEqualTo("userEmail",auth.currentUser!!.email).get(Source.SERVER).addOnCompleteListener {
                if(it.isSuccessful && !it.result.documents.isNullOrEmpty()) {
                    val mail = it.result.documents[0].getString("userEmail") as String
                    val image = it.result.documents[0].getString("userProfileImage") as String
                    val userDetails = UserDetails(mail,image)
                    Log.i("API-TEST", image)
                    userDetailsArray.add(userDetails)
                    Glide
                        .with(requireActivity())
                        .load(image)
                        .into(binding.userEditImage)
                }
            }
    }

}