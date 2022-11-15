package com.seif.cleanarchitecturenoteappwithfirebase

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentForgetPasswordBinding
import com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.forget_password.ForgetPasswordState
import com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.forget_password.ForgetPasswordViewModel
import com.seif.cleanarchitecturenoteappwithfirebase.utils.hide
import com.seif.cleanarchitecturenoteappwithfirebase.utils.show
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {
    lateinit var binding: FragmentForgetPasswordBinding
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels()
    private val TAG = "ForgetPasswordFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collect()
        binding.btnSend.setOnClickListener {
            val email = binding.etEmail.text.toString()
            forgetPasswordViewModel.forgetPassword(email)
        }
    }

    private fun collect() {
        forgetPasswordViewModel.forgetPasswordState
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach {
                when (it) {
                    ForgetPasswordState.Init -> Unit
                    is ForgetPasswordState.IsLoading -> handleLoadingState(it.isLoading)
                    is ForgetPasswordState.SendEmail -> {
                        binding.root.showSnackBar(it.message)
                        Log.d(TAG, "observe: $it")
                    }
                    is ForgetPasswordState.ShowError -> binding.root.showSnackBar(it.message)
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> binding.forgetPasswordProgressBar.show()
            false -> binding.forgetPasswordProgressBar.hide()
        }
    }
}