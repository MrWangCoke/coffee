package com.mrwang.coffeeapp.presentation.screens.detailsscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mrwang.coffeeapp.data.local.AppDatabase
import com.mrwang.coffeeapp.data.repository.CoffeeRepository
import com.mrwang.coffeeapp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailsUiState(
    val isLoading: Boolean = true,
    val product: Product? = null,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CoffeeRepository(AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun fetchProductById(productId: Int) {
        viewModelScope.launch {
            val cachedProduct = repository.getCachedProductById(productId)
            _uiState.update {
                it.copy(
                    isLoading = cachedProduct == null,
                    isRefreshing = true,
                    product = cachedProduct,
                    errorMessage = null
                )
            }

            val result = repository.refreshProductById(productId)
            result.onSuccess { remoteProduct ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        product = remoteProduct ?: cachedProduct,
                        errorMessage = if (remoteProduct == null && cachedProduct == null) "Product not found" else null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        product = cachedProduct,
                        errorMessage = if (cachedProduct == null) {
                            throwable.message ?: "Failed to load product details"
                        } else {
                            null
                        }
                    )
                }
            }
        }
    }
}
