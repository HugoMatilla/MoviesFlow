package com.hugomatilla.moviesflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel() {
    lateinit var data: LiveData<List<Movie>>
        private set

    init {
        val dao = DB.getInstance().movieDao()
        viewModelScope.launch() {
            data = dao.getAll().conflate().asLiveData()
        }
    }
}