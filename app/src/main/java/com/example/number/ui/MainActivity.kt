package com.example.number.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.number.R
import com.example.number.util.ProgressDialogWithTimeout

class MainActivity : AppCompatActivity() {

    lateinit var progressDialogWithTimeout: ProgressDialogWithTimeout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialogWithTimeout = ProgressDialogWithTimeout(this)

        replaceFragment(NumberFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment_container, fragment)
        transaction.commit()
    }
}