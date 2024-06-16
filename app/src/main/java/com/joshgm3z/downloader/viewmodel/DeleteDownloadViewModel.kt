package com.joshgm3z.downloader.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.downloader.model.DownloadRepository
import com.joshgm3z.downloader.model.room.data.DownloadTask
import com.joshgm3z.downloader.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteDownloadViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var downloadRepository: DownloadRepository

    fun deleteAll() {
        viewModelScope.launch {
            downloadRepository.deleteAll()
        }
    }
}