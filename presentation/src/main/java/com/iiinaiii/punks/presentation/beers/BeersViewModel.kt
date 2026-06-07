package com.iiinaiii.punks.presentation.beers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiinaiii.punks.domain.SearchBeersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.iiinaiii.punks.domain.model.Result
import com.iiinaiii.punks.presentation.beers.model.BeerResultUiModel
import com.iiinaiii.punks.presentation.beers.model.UiState
import com.iiinaiii.punks.presentation.util.event.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BeersViewModel @Inject constructor(
    private val searchBeers: SearchBeersUseCase,
) : ViewModel(), DataLoadingSubject {

    private val _uiModel = MutableStateFlow(UiState())
    val uiModel: StateFlow<UiState> = _uiModel.asStateFlow()

    override var isDataLoading = false
    private var isLastPageLoaded = false
    private var pageNum = 1

    fun loadBeers() {
        if (isLastPageLoaded) {
            return
        }
        getBeers(pageNum)
    }

    private fun getBeers(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) { showLoading(page == 1) }
        isDataLoading = true

        val result = searchBeers(page)
        withContext(Dispatchers.Main) {
            if (result is Result.Success) {
                pageNum++
                emitUiModel(showSuccess = Event(BeerResultUiModel(result.data.toItemUiModels())))
            } else {
                emitUiModel(showError = Event(Unit))
                isLastPageLoaded = true
            }
            isDataLoading = false
        }
    }

    private fun showLoading(isInitial: Boolean) {
        if (isInitial) {
            emitUiModel(showProgress = true)
        } else {
            emitUiModel(showFooterProgress = true)
        }
    }

    private fun emitUiModel(
        showProgress: Boolean = false,
        showFooterProgress: Boolean = false,
        showSuccess: Event<BeerResultUiModel>? = null,
        showError: Event<Unit>? = null
    ) {
        _uiModel.value = UiState(showProgress, showFooterProgress, showSuccess, showError)
    }
}
