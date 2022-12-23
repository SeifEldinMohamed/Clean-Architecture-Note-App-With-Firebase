package com.seif.cleanarchitecturenoteappwithfirebase.presentation.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.seif.cleanarchitecturenoteappwithfirebase.R
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.FragmentLoginBinding
import com.seif.cleanarchitecturenoteappwithfirebase.utils.hide
import com.seif.cleanarchitecturenoteappwithfirebase.utils.show
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showSnackBar
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val TAG = "loginFragment"
    private lateinit var googleSignInClient : GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
        binding.tvHaveNotRegistered.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentFirebaseUser = loginViewModel.getCurrentUser()
        if (currentFirebaseUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_noteListFragment)
        }
    }

    private fun observe() {
        loginViewModel.loginState.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach {
                when (it) {
                    LoginFragmentState.Init -> Unit
                    is LoginFragmentState.IsLoading -> handleLoadingState(it.isLoading)
                    is LoginFragmentState.LoggedInSuccessfully -> {
                        requireContext().showToast("Welcome back ❤")
                        findNavController().navigate(R.id.action_loginFragment_to_noteListFragment)
                        Log.d(TAG, "observe: $it")
                    }
                    is LoginFragmentState.ShowError -> binding.root.showSnackBar(it.message)
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> binding.loginProgressBar.show()
            false -> binding.loginProgressBar.hide()
        }
    }

    private fun loginUser() {
        val email: String = binding.etEmail.text.toString()
        val password: String = binding.etPassword.text.toString()
        loginViewModel.loginUser(email, password)
    }

//    private fun signInGoogle(){
//        val signInIntent = googleSignInClient.signInIntent
//        launcher.launch(signInIntent)
//    }
//
//    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            result ->
//        if (result.resultCode == Activity.RESULT_OK){
//
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            handleResults(task)
//        }
//    }
//
//    private fun handleResults(task: Task<GoogleSignInAccount>) {
//        if (task.isSuccessful){
//            val account : GoogleSignInAccount? = task.result
//            if (account != null){
//                updateUI(account)
//            }
//        }else{
//            Toast.makeText(requireContext(), task.exception.toString() , Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun updateUI(account: GoogleSignInAccount) {
//        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
//        loginViewModel.signInWithGoogle(credential)
//    }

}