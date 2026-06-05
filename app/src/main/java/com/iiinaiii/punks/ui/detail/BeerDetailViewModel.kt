package com.iiinaiii.punks.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiinaiii.punks.data.Result
import com.iiinaiii.punks.domain.GetBeerUseCase
import com.iiinaiii.punks.domain.model.Beer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBeer: GetBeerUseCase
) : ViewModel() {

    private val beerId: Int = savedStateHandle.get<Int>("extra_beer_id") ?: -1

    private val _uiModel = MutableLiveData<BeerDetailUiModel>()
    val uiModel: LiveData<BeerDetailUiModel>
        get() = _uiModel

    init {
        if (beerId != -1) {
            loadBeer(beerId)
        }
    }

    private fun loadBeer(beerId: Int) = viewModelScope.launch(Dispatchers.Default) {
        when (val result = getBeer(beerId)) {
            is Result.Success -> emitUiModel(result.data)
            is Result.Error -> throw result.exception
        }
    }

    private fun emitUiModel(beer: Beer) {
        _uiModel.postValue(BeerDetailUiModel(beer))
    }

    data class BeerDetailUiModel(
        val beer: Beer
    )
}