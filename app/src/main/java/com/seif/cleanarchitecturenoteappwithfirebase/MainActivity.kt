package com.seif.cleanarchitecturenoteappwithfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.seif.cleanarchitecturenoteappwithfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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