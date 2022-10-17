package com.test.loginapp.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.test.loginapp.R
import com.test.loginapp.databinding.FragmentLoginBinding
import com.test.loginapp.util.LetterWatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Login : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        observeStates()
        clickEvents()
        textWatchers()
    }

    private fun clickEvents() = binding.apply {
        login.setOnClickListener { viewModel.login() }
    }

    private fun observeStates() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.apply {
                binding.apply {
                    launch {
                        loginState.collect {
                            progress.visibility = if (it.loading) View.VISIBLE else View.GONE
                            if (it.loginMessage.isNotEmpty())
                                Snackbar.make(root, it.loginMessage, Snackbar.LENGTH_SHORT).show()
                            if (it.error.isNotEmpty())
                                Snackbar.make(root, it.error, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    launch {
                        detailState.collect {
                            emailInput.error = it.emailValid
                            passInput.error = it.passwordValid
                        }
                    }
                }
            }
        }
    }

    private fun textWatchers() = binding.apply {
        emailInput.addTextChangedListener(object : LetterWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.checkEmail(s.toString())
            }
        })
        passInput.addTextChangedListener(object : LetterWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.checkPassword(s.toString())
            }
        })
    }

}