package com.example.number.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.number.dao.AppDatabase
import com.example.number.dao.model.NumberModel
import com.example.number.data.network.Result
import com.example.number.data.repository.NetworkRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody


class NumberViewModel(
    app: Application,
    private val networkRepository: NetworkRepository,
    private val database: AppDatabase
) : AndroidViewModel(app) {

    var number: Int? = null

    private val numbersModelSet = mutableSetOf<NumberModel>()

    // LiveData for show progressBar
    private val _showProgressEvent = MutableLiveData<Boolean>()
    val showProgressEvent: LiveData<Boolean> = _showProgressEvent

    // LiveData for show progressBar
    private val _showErrorEvent = MutableLiveData<Unit>()
    val showErrorEvent: LiveData<Unit> = _showErrorEvent


    // LiveData for API getNumberFact
    private val _getNumberListResult = MutableLiveData<List<NumberModel>>()
    val getNumberListResult: LiveData<List<NumberModel>> = _getNumberListResult

    // LiveData for API setAccount
    private val _setAccountResult = MutableLiveData<Unit>()
    val setAccountResult: LiveData<Unit> = _setAccountResult

    // Error handler for coroutines
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _showErrorEvent.value = Unit
    }

    // Save number to DB
    private fun saveNumberToDB(numberModel: NumberModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.numberDao().insertAll(numberModel)
            }
        }
    }

    // Get all number items from DB
    fun getAllNumbersFromDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                numbersModelSet.addAll(database.numberDao().getAll())
                _getNumberListResult.postValue(numbersModelSet.toList())
            }
        }
    }

    // API get number fact by inserted number
    fun getNumberFact() {
        _showProgressEvent.value = true

        // If no inserted number, show error
        if (number == null) {
            _showProgressEvent.value = false
            _showErrorEvent.value = Unit
            return
        }

        viewModelScope.let { coroutineScope ->
            coroutineScope.launch(coroutineExceptionHandler) {
                number?.let { number ->
                    networkRepository.getNumberFact(number).let { result ->
                        _showProgressEvent.value = false

                        when (result) {
                            is Result.Success<ResponseBody> -> {

                                // Create numberModel obj
                                val numberModel =
                                    NumberModel(id = number, info = result.data.string())

                                // Save model and update UI
                                saveNumberAndUpdateUI(numberModel)
                            }

                            is Result.Error -> {
                                // Show error
                                _showErrorEvent.value = Unit
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    fun getRandomFact() {
        _showProgressEvent.value = true

        viewModelScope.let { coroutineScope ->
            coroutineScope.launch(coroutineExceptionHandler) {
                networkRepository.getRandomFact().let { result ->
                    _showProgressEvent.value = false

                    when (result) {
                        is Result.Success<ResponseBody> -> {
                            val rawString = result.data.string()
                            val elements = rawString.split(" ", limit = 2)

                            // In case of errors of the returned data
                            if (elements.isEmpty() || elements.size == 1) {
                                return@launch
                            }

                            try {
                                // Create NumberModel obj
                                val numberInt = elements[0].toInt()
                                val numberModel = NumberModel(id = numberInt, info = rawString)

                                // Save model and update UI
                                saveNumberAndUpdateUI(numberModel)

                            } catch (e: NumberFormatException) {
                                _showErrorEvent.value = Unit
                            }
                        }

                        is Result.Error -> {
                            _showErrorEvent.value = Unit
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    /**
     * Saves a NumberModel object to the database and updates the UI.
     *
     * @param numberModel The NumberModel object to save and display.
     */
    private fun saveNumberAndUpdateUI(numberModel: NumberModel) {
        // Save to DB
        saveNumberToDB(numberModel)
        // Add the NumberModel object to a Set containing all DB numbers
        numbersModelSet.add(numberModel)
        // Convert the Set to a List and update the LiveData object to reflect changes in the UI
        _getNumberListResult.value = numbersModelSet.toList()
    }
}
