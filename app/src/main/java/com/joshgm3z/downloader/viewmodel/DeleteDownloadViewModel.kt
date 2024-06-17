package com.joshgm3z.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.downloader.model.repo.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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