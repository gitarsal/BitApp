package com.project.bitapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.bitapp.databinding.ActivityMainBinding
import com.project.bitapp.presentation.viewmodel.PairListingAndDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pairListingAndDetailViewModel: PairListingAndDetailViewModel by viewModels()
        pairListingAndDetailViewModel.connectSocket(null)
    }
}
