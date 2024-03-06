package com.example.number.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.number.application.NumberApplication
import com.example.number.ui.NumberDetailViewModel
import com.example.number.ui.NumberViewModel

class ViewModelFactory(
    private val app: NumberApplication
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {

            NumberViewModel::class.java -> {
                NumberViewModel(app, app.networkRepository, app.database)
            }

            NumberDetailViewModel::class.java -> {
                NumberDetailViewModel(app)
            }

            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }

}

fun Fragment.factory() =
    ViewModelFactory(requireContext().applicationContext as NumberApplication)