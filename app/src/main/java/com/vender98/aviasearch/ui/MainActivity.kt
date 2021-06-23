package com.vender98.aviasearch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vender98.aviasearch.R
import com.vender98.aviasearch.ui.screens.choosedestination.ChooseDestinationFragment

class MainActivity : AppCompatActivity() {

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ChooseDestinationFragment())
                .commit()
        }
    }
}