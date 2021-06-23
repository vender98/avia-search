package com.vender98.aviasearch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.vender98.aviasearch.R
import com.vender98.aviasearch.databinding.ActivityMainBinding
import com.vender98.aviasearch.di.ToothpickViewModelFactory
import com.vender98.aviasearch.presentation.MainViewModel
import com.vender98.aviasearch.ui.screens.choosedestination.ChooseDestinationFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container)
    private val binding: ActivityMainBinding by viewBinding(R.id.fragment_container)
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ChooseDestinationFragment())
                .commit()
        }

        viewModel =
            ViewModelProvider(this, ToothpickViewModelFactory()).get(MainViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.errorsFlow.collect { showError(it) }
        }
    }

    private fun showError(message: String) {
        Snackbar
            .make(binding.fragmentContainer, message, Snackbar.LENGTH_LONG)
            .show()
    }
}