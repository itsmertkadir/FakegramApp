package com.mertkadir.fakegramapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.mertkadir.fakegramapp.databinding.FragmentAddPostBinding


class AddPostFragment : Fragment() {

    private lateinit var binding : FragmentAddPostBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
            if (ContextCompat.checkSelfPermission(it.context,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(it,"Give Permission",Snackbar.LENGTH_INDEFINITE).setAction("GIVE PERMISSION",View.OnClickListener {
                        //request permission

                    }).show()
                }else{
                    //request permission

                }

            }else{
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //intent start

            }


        }


    }


}