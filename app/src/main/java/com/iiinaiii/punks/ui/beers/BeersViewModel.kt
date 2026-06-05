package com.iiinaiii.punks.ui.beers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiinaiii.punks.domain.SearchBeersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.iiinaiii.punks.domain.model.Result
import com.iiinaiii.punks.ui.beers.model.BeerResultUiModel
import com.iiinaiii.punks.ui.beers.model.HomeUiModel
import com.iiinaiii.punks.ui.util.event.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BeersViewModel @Inject constructor(
    private val searchBeers: SearchBeersUseCase,
) : ViewModel(), DataLoadingSubject {

    private val _uiModel = MutableLiveData<HomeUiModel>()
    val uiModel: LiveData<HomeUiModel>
        get() = _uiModel

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
        withContext(Dispatchers.Main) { showLoading() }
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

    private fun showLoading() {
        emitUiModel(showProgress = true)
    }

    private fun emitUiModel(
        showProgress: Boolean = false,
        showSuccess: Event<BeerResultUiModel>? = null,
        showError: Event<Unit>? = null
    ) {
        _uiModel.postValue(HomeUiModel(showProgress, showSuccess, showError))
    }
}
