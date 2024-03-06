package com.example.number.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.number.dao.model.NumberModel

class NumberDetailViewModel(
    app: Application
) : AndroidViewModel(app) {

    // LiveData numberModel
    val numberModelData = MutableLiveData<NumberModel>()
}
