package com.rustan.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailScreenViewModel : ViewModel() {
    private val _isAudioPlayerVisible = MutableStateFlow(false)
    val isAudioPlayerVisible: StateFlow<Boolean> = _isAudioPlayerVisible.asStateFlow()

    fun onEvent(detailEvent: DetailEvent) {
        when (detailEvent) {
            is DetailEvent.onPlay -> {}
            is DetailEvent.onPrevious -> {}
            is DetailEvent.onNext -> {}
            is DetailEvent.onPlayerVisible -> {
                _isAudioPlayerVisible.value = !_isAudioPlayerVisible.value
            }
        }
    }
}

sealed interface DetailEvent {
    object onPlay : DetailEvent
    object onPrevious : DetailEvent
    object onNext : DetailEvent
    object onPlayerVisible : DetailEvent
}
