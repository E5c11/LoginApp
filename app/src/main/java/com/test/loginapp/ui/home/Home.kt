package com.test.loginapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.loginapp.R
import com.test.loginapp.databinding.FragmentHomeBinding
import com.test.loginapp.util.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.login.setOnClickListener {
            findNavController().navigate(HomeDirections.actionHomeToLogin())
        }
        lifecycleScope.launchWhenStarted {
            viewModel.networkState.collect{
                Log.d("myT", "onViewCreated: $it")
                when (it) {
                    is NetworkStatus.Available -> Snackbar.make(binding.root, "Network available", Snackbar.LENGTH_SHORT).show()
                    is NetworkStatus.Unavailable -> Snackbar.make(binding.root, "Network unavailable", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        }
    }

}