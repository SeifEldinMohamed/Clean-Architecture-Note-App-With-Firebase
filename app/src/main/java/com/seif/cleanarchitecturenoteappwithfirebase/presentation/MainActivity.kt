package com.seif.cleanarchitecturenoteappwithfirebase.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.seif.cleanarchitecturenoteappwithfirebase.R
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.ActivityMainBinding
import com.seif.cleanarchitecturenoteappwithfirebase.utils.SharedPrefs
import com.seif.cleanarchitecturenoteappwithfirebase.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    @Inject
    lateinit var sharedPrefs: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefs.put("firstTime", true)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
    }
}
/*
*       for(i in 0..6){
//           val user =  User(
//                "User $i",
//                "user$i@gmail.com",
//                "user_$i",
//                i%2 ==0
//            )
//            FirebaseFirestore.getInstance().collection("users")
//                .add(user)
//                .addOnSuccessListener { documentReference ->
//                    Log.d("Main", "onCreate: Document Snapshot added with id: $documentReference")
//                }
//                .addOnFailureListener{ e ->
//                    Log.d("Main", "onCreate: Error Adding Document $e")
//                }
//        }
* */
