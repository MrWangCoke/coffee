package com.mrwang.coffeeapp.presentation.screens.detailsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrwang.coffeeapp.data.network.NetworkManager
import com.mrwang.coffeeapp.data.network.SupabaseConfig
import com.mrwang.coffeeapp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailsUiState(
    val isLoading: Boolean = true,
    val product: Product? = null,
    val errorMessage: String? = null
)

class DetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun fetchProductById(productId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, product = null, errorMessage = null) }
            try {
                val products = NetworkManager.api.getProductById(
                    apiKey = SupabaseConfig.ANON_KEY,
                    authorization = SupabaseConfig.authorizationHeader,
                    idFilter = "eq.$productId"
                )

                val targetProduct = products.firstOrNull()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = targetProduct,
                        errorMessage = if (targetProduct == null) "Product not found" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = null,
                        errorMessage = e.message ?: "Failed to load product details"
                    )
                }
            }
        }
    }
}
