package com.rustan.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustan.domain.GetDataUseCase
import com.rustan.domain.NetworkResult
import com.rustan.model.ImageItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(getDataUseCase: GetDataUseCase) : ViewModel() {
    private val _navigateTo: MutableSharedFlow<HomeNavigationEvent> = MutableSharedFlow()
    val navigateTo: SharedFlow<HomeNavigationEvent> = _navigateTo.asSharedFlow()

    val uiState = combine(
        getDataUseCase("cat"),
        getDataUseCase("dog"),
        getDataUseCase("elephant"),
        getDataUseCase("hamster")
    ) { catResult, dogResult, elephantResult, hamsterResult ->
        // Check if any of the results are errors
        val errorResult =
            listOf(
                catResult,
                dogResult,
                elephantResult,
                hamsterResult
            ).find { it is NetworkResult.Error }
        if (errorResult is NetworkResult.Error) {
            return@combine HomeScreenState.Error
        }

        // If no errors, check if all are successful
        if (catResult is NetworkResult.Success &&
            dogResult is NetworkResult.Success &&
            elephantResult is NetworkResult.Success &&
            hamsterResult is NetworkResult.Success
        ) {
            HomeScreenState.Success(
                homeData = HomeData(
                    row1 = catResult.data,
                    row2 = dogResult.data,
                    row3 = elephantResult.data,
                    row4 = hamsterResult.data
                )
            )
        } else {
            HomeScreenState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenState.Loading,
    )

    fun onEvent(homeEvent: HomeEvent) {
        when (homeEvent) {
            is HomeEvent.CardClick -> {
                viewModelScope.launch {
                    _navigateTo.emit(HomeNavigationEvent.NavigateToDetails(homeEvent.imageItem))
                }
            }
        }
    }
}

sealed interface HomeScreenState {
    data class Success(val homeData: HomeData) : HomeScreenState
    data object Error : HomeScreenState
    data object Loading : HomeScreenState
    data object Empty : HomeScreenState
}

data class HomeData(
    val row1: List<ImageItem>,
    val row2: List<ImageItem>,
    val row3: List<ImageItem>,
    val row4: List<ImageItem>
)

sealed class HomeEvent {
    class CardClick(val imageItem: ImageItem) : HomeEvent()
}

sealed class HomeNavigationEvent {
    class NavigateToDetails(val imageItem: ImageItem) : HomeNavigationEvent()
}