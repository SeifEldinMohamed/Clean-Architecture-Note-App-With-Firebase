package com.seif.cleanarchitecturenoteappwithfirebase.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.seif.cleanarchitecturenoteappwithfirebase.R
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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