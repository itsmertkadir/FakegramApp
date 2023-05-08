package com.mertkadir.fakegramapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

import com.mertkadir.fakegramapp.databinding.FragmentProfileSettingsBinding


import com.mertkadir.fakegramapp.model.UserDetails



class ProfileSettingsFragment : Fragment() {

    private lateinit var binding : FragmentProfileSettingsBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseStorage
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var resultActivityLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    private  var selectedImage : Uri? = null
    private lateinit var userDetailsArray: ArrayList<UserDetails>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.storage
        fireStore = Firebase.firestore
        userDetailsArray = ArrayList<UserDetails>()
        getData()
        registerLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.exitSettings.setOnClickListener {
            val action = ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToProfileFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.signOut.setOnClickListener{
            auth.signOut()
            val action = ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToRegisterActivity()
            Navigation.findNavController(it).navigate(action)
            requireActivity().finish()

        }

        binding.editSave.setOnClickListener {
            val editEmail = binding.userProfileEmailEdit.text.toString()
            val editPassword = binding.userProfilePassEdit.text.toString()

            if (auth.currentUser != null){
                val user = auth.currentUser

                val userUUID = user!!.uid
                val imageName = "$userUUID.jpg"

                val dbReference = db.reference
                val imageReference: StorageReference = dbReference.child("userProfileImage").child(imageName)

                if (selectedImage != null) {
                    imageReference.putFile(selectedImage!!).addOnSuccessListener {

                        val uploadImageReference = db.reference.child("userProfileImage").child(imageName)

                        uploadImageReference.downloadUrl.addOnSuccessListener {

                            val userProfileImageDownload = it.toString()

                            val userDetail = HashMap<String, Any>()
                            userDetail.put("userEmail",user!!.email!!)
                            userDetail.put("userUUID", userUUID)
                            userDetail.put("userProfileImage",userProfileImageDownload)




                            fireStore.collection("UserInfo").document(auth.currentUser!!.uid).set(userDetail).addOnSuccessListener {

                                Toast.makeText(this.context, "Update User Details", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(this.context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                            }


                        }

                    }.addOnFailureListener {
                        Toast.makeText(this.context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }

        binding.userImageEdit.setOnClickListener {

            if (Build.VERSION.SDK_INT >= 33) {
                //sdk 33+

                if (ContextCompat.checkSelfPermission(
                        it.context,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this.requireActivity(),
                            Manifest.permission.READ_MEDIA_IMAGES
                        )
                    ) {
                        Snackbar.make(it, "Give Permission", Snackbar.LENGTH_INDEFINITE)
                            .setAction("GIVE PERMISSION", View.OnClickListener {
                                //request permission
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }).show()
                    } else {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }

                } else {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    //intent start
                    resultActivityLauncher.launch(intentToGallery)

                }

            } else {
                //sdk 33-
                if (ContextCompat.checkSelfPermission(
                        it.context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this.requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        Snackbar.make(it, "Give Permission", Snackbar.LENGTH_INDEFINITE)
                            .setAction("GIVE PERMISSION", View.OnClickListener {
                                //request permission
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }).show()
                    } else {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }

                } else {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    //intent start
                    resultActivityLauncher.launch(intentToGallery)
                }

            }


        }

    }


    private fun getData(){
        fireStore.collection("UserInfo").whereEqualTo("userEmail",auth.currentUser!!.email).get(Source.SERVER).addOnCompleteListener {
                if(it.isSuccessful && !it.result.documents.isNullOrEmpty()) {
                    val mail = it.result.documents[0].getString("userEmail") as String
                    val image = it.result.documents[0].getString("userProfileImage") as String
                    val userDetails = UserDetails(mail,image)
                    Log.i("API-TEST", image)
                    userDetailsArray.add(userDetails)
                    Glide
                        .with(requireActivity())
                        .load(image)
                        .into(binding.userImageEdit)
                }
            }
    }
    private fun registerLauncher(){

        resultActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    selectedImage = intentFromResult.data

                    selectedImage.let {
                        binding.userImageEdit.setImageURI(it)
                    }

                }
            }

        }


        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result){

                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                resultActivityLauncher.launch(intentToGallery)

            }else{
                Toast.makeText(this.context, "Permission needed", Toast.LENGTH_LONG).show()
            }


        }


    }




}