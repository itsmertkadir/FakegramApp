package com.mertkadir.fakegramapp.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.android.material.snackbar.Snackbar
import com.mertkadir.fakegramapp.databinding.FragmentAddPostBinding


class AddPostFragment : Fragment() {

    private lateinit var binding : FragmentAddPostBinding
    private lateinit var resultActivityLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    private  var selectedImage : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homePage.setOnClickListener {
            val action = AddPostFragmentDirections.actionAddPostFragment2ToHomeFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.profilePage.setOnClickListener {
            val action = AddPostFragmentDirections.actionAddPostFragment2ToProfileFragment()
            Navigation.findNavController(it).navigate(action)
        }


        binding.selectImage.setOnClickListener {

            if (Build.VERSION.SDK_INT >= 33){
                //sdk 33+

                if (ContextCompat.checkSelfPermission(it.context,Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)){
                        Snackbar.make(it,"Give Permission",Snackbar.LENGTH_INDEFINITE).setAction("GIVE PERMISSION",View.OnClickListener {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()
                    }else{
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }

                }else{
                    val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    //intent start
                    resultActivityLauncher.launch(intentToGallery)

                }

            }else{
                //sdk 33-
                if (ContextCompat.checkSelfPermission(it.context,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Snackbar.make(it,"Give Permission",Snackbar.LENGTH_INDEFINITE).setAction("GIVE PERMISSION",View.OnClickListener {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                    }else{
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }

                }else{
                    val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    //intent start
                    resultActivityLauncher.launch(intentToGallery)
                }

            }

        }

    }

    private fun registerLauncher(){

        resultActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    selectedImage = intentFromResult.data

                    selectedImage.let {
                        binding.selectImage.setImageURI(it)
                    }

                }
            }

        }


        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->

            if (result){

                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                resultActivityLauncher.launch(intentToGallery)

            }else{
                Toast.makeText(this.context, "Permission needed", Toast.LENGTH_LONG).show()
            }


        }


    }


}