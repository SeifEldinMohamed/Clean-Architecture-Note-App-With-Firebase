package com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentRegisterBinding
import com.seif.cleanarchitecturenoteappwithfirebase.domain.model.User
import com.seif.cleanarchitecturenoteappwithfirebase.utils.hide
import com.seif.cleanarchitecturenoteappwithfirebase.utils.show
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showSnackBar
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun observe() {
        registerViewModel.registerState
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { registerState ->
                when (registerState) {
                    RegisterFragmentState.Init -> Unit
                    is RegisterFragmentState.IsLoading -> handleLoadingState(registerState.isLoading)
                    is RegisterFragmentState.RegisteredSuccessfully -> {
                        requireContext().showToast("Registered Successfully")
                        // navigate to note list ( related to this user )
                        val action = RegisterFragmentDirections.actionRegisterFragmentToNoteListFragment(registerState.registeredUser)
                        findNavController().navigate(action)
                    }
                    is RegisterFragmentState.ShowError -> binding.root.showSnackBar(registerState.message)
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> binding.registerProgressBar.show()
            false -> binding.registerProgressBar.hide()
        }
    }

    private fun registerUser() {
        val user = createUser()
        registerViewModel.registerUser(user)
    }

    private fun createUser(): User {
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        return User(
            id = "",
            username = username,
            email = email,
            password = password,
            subscribed = false
        )
    }
}