package com.mrwang.coffeeapp.presentation.screens.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrwang.coffeeapp.data.network.NetworkManager
import com.mrwang.coffeeapp.data.network.SupabaseConfig
import com.mrwang.coffeeapp.domain.model.CreateOrderItemRequest
import com.mrwang.coffeeapp.domain.model.OrderItem
import com.mrwang.coffeeapp.domain.model.Product
import com.mrwang.coffeeapp.domain.model.UpdateOrderItemQuantityRequest
import com.mrwang.coffeeapp.domain.model.UpdateOrderItemStatusRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ShopUiState(
    val cartItems: List<OrderItem> = emptyList(),
    val pendingOrders: List<OrderItem> = emptyList(),
    val completedOrders: List<OrderItem> = emptyList(),
    val merchantOrders: List<OrderItem> = emptyList(),
    val favouriteProducts: List<Product> = emptyList(),
    val cartSelections: Map<Long, ProductSelection> = emptyMap(),
    val favouriteSelections: Map<Int, ProductSelection> = emptyMap(),
    val isCartLoading: Boolean = false,
    val isOrdersLoading: Boolean = false,
    val isMerchantOrdersLoading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)

data class ProductSelection(
    val size: String = "M",
    val temperature: String = "Hot"
)

class ShopViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()
    private val productCache = mutableMapOf<Long, Product>()

    fun clearMessages() {
        _uiState.update { it.copy(message = null, errorMessage = null) }
    }

    fun toggleFavourite(
        product: Product,
        size: String = "M",
        temperature: String = "Hot"
    ) {
        productCache[product.id.toLong()] = product
        _uiState.update { currentState ->
            val exists = currentState.favouriteProducts.any { it.id == product.id }
            currentState.copy(
                favouriteProducts = if (exists) {
                    currentState.favouriteProducts.filterNot { it.id == product.id }
                } else {
                    currentState.favouriteProducts + product
                },
                favouriteSelections = if (exists) {
                    currentState.favouriteSelections - product.id
                } else {
                    currentState.favouriteSelections + (product.id to ProductSelection(size, temperature))
                },
                message = if (exists) "Removed from favourites" else "Added to favourites",
                errorMessage = null
            )
        }
    }

    fun removeFavourite(product: Product) {
        _uiState.update { currentState ->
            currentState.copy(
                favouriteProducts = currentState.favouriteProducts.filterNot { it.id == product.id },
                favouriteSelections = currentState.favouriteSelections - product.id,
                message = "Removed from favourites",
                errorMessage = null
            )
        }
    }

    fun loadCart(userId: String?, accessToken: String?) {
        if (userId.isNullOrBlank() || accessToken.isNullOrBlank()) {
            _uiState.update { it.copy(cartItems = emptyList(), isCartLoading = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCartLoading = true, errorMessage = null) }
            try {
                val items = NetworkManager.api.getCartItems(
                    apiKey = SupabaseConfig.ANON_KEY,
                    authorization = "Bearer $accessToken",
                    userIdFilter = "eq.$userId"
                )
                val hydratedItems = hydrateCartProducts(items, accessToken)
                _uiState.update { currentState ->
                    val selections = hydratedItems.fold(currentState.cartSelections) { selections, item ->
                        if (item.productId in selections) selections else selections + (item.productId to ProductSelection())
                    }
                    currentState.copy(
                        cartItems = hydratedItems,
                        cartSelections = selections,
                        isCartLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCartLoading = false,
                        errorMessage = "Failed to load cart"
                    )
                }
            }
        }
    }

    fun loadCompletedOrders(userId: String?, accessToken: String?) {
        if (userId.isNullOrBlank() || accessToken.isNullOrBlank()) {
            _uiState.update { it.copy(completedOrders = emptyList(), isOrdersLoading = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isOrdersLoading = true, errorMessage = null) }
            try {
                val items = NetworkManager.api.getCompletedOrderItems(
                    apiKey = SupabaseConfig.ANON_KEY,
                    authorization = "Bearer $accessToken",
                    userIdFilter = "eq.$userId"
                )
                _uiState.update {
                    it.copy(
                        completedOrders = hydrateCartProducts(items, accessToken),
                        isOrdersLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isOrdersLoading = false, errorMessage = "Failed to load orders") }
            }
        }
    }

    fun loadPendingOrders(userId: String?, accessToken: String?) {
        if (userId.isNullOrBlank() || accessToken.isNullOrBlank()) {
            _uiState.update { it.copy(pendingOrders = emptyList(), isOrdersLoading = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isOrdersLoading = true, errorMessage = null) }
            try {
                val items = NetworkManager.api.getPendingOrderItems(
                    apiKey = SupabaseConfig.ANON_KEY,
                    authorization = "Bearer $accessToken",
                    userIdFilter = "eq.$userId"
                )
                _uiState.update {
                    it.copy(
                        pendingOrders = hydrateCartProducts(items, accessToken),
                        isOrdersLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isOrdersLoading = false, errorMessage = "Failed to load pending orders") }
            }
        }
    }

    fun loadOrderLists(userId: String?, accessToken: String?) {
        loadPendingOrders(userId, accessToken)
        loadCompletedOrders(userId, accessToken)
    }

    fun loadMerchantOrders(merchantId: String?, accessToken: String?) {
        if (merchantId.isNullOrBlank() || accessToken.isNullOrBlank()) {
            _uiState.update { it.copy(merchantOrders = emptyList(), isMerchantOrdersLoading = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isMerchantOrdersLoading = true, errorMessage = null) }
            try {
                val items = NetworkManager.api.getMerchantCompletedOrderItems(
                    apiKey = SupabaseConfig.ANON_KEY,
                    authorization = "Bearer $accessToken",
                    merchantIdFilter = "eq.$merchantId"
                )
                _uiState.update {
                    it.copy(
                        merchantOrders = hydrateCartProducts(items, accessToken),
                        isMerchantOrdersLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isMerchantOrdersLoading = false, errorMessage = "Failed to load merchant orders") }
            }
        }
    }

    fun completeCartOrder(userId: String?, accessToken: String?) {
        if (userId.isNullOrBlank() || accessToken.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Please log in before placing an order") }
            return
        }

        val currentCartItems = _uiState.value.cartItems
        if (currentCartItems.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Cart is empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCartLoading = true, errorMessage = null, message = null) }
            try {
                currentCartItems.forEach { item ->
                    NetworkManager.api.updateOrderItemStatus(
                        apiKey = SupabaseConfig.ANON_KEY,
                        authorization = "Bearer $accessToken",
                        idFilter = "eq.${item.id}",
                        request = UpdateOrderItemStatusRequest(status = "completed")
                    )
                }

                val completedItems = currentCartItems.map { it.copy(status = "completed") }
                _uiState.update { currentState ->
                    currentState.copy(
                        cartItems = emptyList(),
                        cartSelections = emptyMap(),
                        completedOrders = completedItems + currentState.completedOrders,
                        isCartLoading = false,
                        message = "Order completed",
                        errorMessage = null
                    )
                }
                loadCompletedOrders(userId, accessToken)
            } catch (e: Exception) {
                _uiState.update { it.copy(isCartLoading = false, errorMessage = "Failed to complete order") }
            }
        }
    }

    fun saveCartAsPendingOrder(userId: String?, accessToken: String?) {
        if (userId.isNullOrBlank() || accessToken.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Please log in before placing an order") }
            return
        }

        val currentCartItems = _uiState.value.cartItems
        if (currentCartItems.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Cart is empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCartLoading = true, errorMessage = null, message = null) }
            try {
                currentCartItems.forEach { item ->
                    NetworkManager.api.updateOrderItemStatus(
                        apiKey = SupabaseConfig.ANON_KEY,
                        authorization = "Bearer $accessToken",
                        idFilter = "eq.${item.id}",
                        request = UpdateOrderItemStatusRequest(status = "pending")
                    )
                }

                val pendingItems = currentCartItems.map { it.copy(status = "pending") }
                _uiState.update { currentState ->
                    currentState.copy(
                        cartItems = emptyList(),
                        cartSelections = emptyMap(),
                        pendingOrders = pendingItems + currentState.pendingOrders,
                        isCartLoading = false,
                        message = "Order saved as pending",
                        errorMessage = null
                    )
                }
                loadPendingOrders(userId, accessToken)
            } catch (e: Exception) {
                _uiState.update { it.copy(isCartLoading = false, errorMessage = "Failed to save pending order") }
            }
        }
    }

    fun payPendingOrder(item: OrderItem, userId: String?, accessToken: String?) {
        if (userId.isNullOrBlank() || accessToken.isNullOrBlank()) return

        viewModelScope.launch {
            try {
                NetworkManager.api.updateOrderItemStatus(
                    apiKey = SupabaseConfig.ANON_KEY,
                    authorization = "Bearer $accessToken",
                    idFilter = "eq.${item.id}",
                    request = UpdateOrderItemStatusRequest(status = "completed")
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        pendingOrders = currentState.pendingOrders.filterNot { it.id == item.id },
                        completedOrders = listOf(item.copy(status = "completed")) + currentState.completedOrders,
                        errorMessage = null,
                        message = "Order completed"
                    )
                }
                loadOrderLists(userId, accessToken)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to pay order") }
            }
        }
    }

    fun cancelPendingOrder(item: OrderItem, userId: String?, accessToken: String?) {
        if (userId.isNullOrBlank() || accessToken.isNullOrBlank()) return

        viewModelScope.launch {
            try {
                NetworkManager.api.deleteCartItem(
                    apiKey = SupabaseConfig.ANON_KEY,
                    authorization = "Bearer $accessToken",
                    idFilter = "eq.${item.id}"
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        pendingOrders = currentState.pendingOrders.filterNot { it.id == item.id },
                        errorMessage = null,
                        message = "Pending order canceled"
                    )
                }
                loadPendingOrders(userId, accessToken)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to cancel pending order") }
            }
        }
    }

    fun addToCart(
        product: Product,
        userId: String?,
        accessToken: String?,
        size: String = "M",
        temperature: String = "Hot"
    ) {
        productCache[product.id.toLong()] = product
        if (userId.isNullOrBlank() || accessToken.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Please log in before adding items to cart") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    cartSelections = it.cartSelections + (product.id.toLong() to ProductSelection(size, temperature)),
                    errorMessage = null,
                    message = null
                )
            }
            try {
                val existingItem = _uiState.value.cartItems.firstOrNull { it.productId == product.id.toLong() }
                if (existingItem != null) {
                    changeCartQuantity(
                        item = existingItem,
                        quantity = existingItem.quantity + 1,
                        accessToken = accessToken,
                        showSuccessMessage = true
                    )
                } else {
                    NetworkManager.api.addCartItem(
                        apiKey = SupabaseConfig.ANON_KEY,
                        authorization = "Bearer $accessToken",
                        request = CreateOrderItemRequest(
                            userId = userId,
                            productId = product.id.toLong(),
                            merchantId = product.merchantId
                        )
                    )
                    loadCart(userId, accessToken)
                    _uiState.update { it.copy(message = "Added to cart") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to add item to cart") }
            }
        }
    }

    fun increaseCartItem(item: OrderItem, accessToken: String?) {
        if (accessToken.isNullOrBlank()) return

        viewModelScope.launch {
            changeCartQuantity(
                item = item,
                quantity = item.quantity + 1,
                accessToken = accessToken,
                showSuccessMessage = false
            )
        }
    }

    fun decreaseCartItem(item: OrderItem, accessToken: String?) {
        if (accessToken.isNullOrBlank()) return

        viewModelScope.launch {
            if (item.quantity <= 1) {
                deleteCartItem(item, accessToken)
            } else {
                changeCartQuantity(
                    item = item,
                    quantity = item.quantity - 1,
                    accessToken = accessToken,
                    showSuccessMessage = false
                )
            }
        }
    }

    fun removeCartItem(item: OrderItem, accessToken: String?) {
        if (accessToken.isNullOrBlank()) return

        viewModelScope.launch {
            deleteCartItem(item, accessToken)
        }
    }

    private suspend fun changeCartQuantity(
        item: OrderItem,
        quantity: Int,
        accessToken: String,
        showSuccessMessage: Boolean
    ) {
        try {
            NetworkManager.api.updateCartItemQuantity(
                apiKey = SupabaseConfig.ANON_KEY,
                authorization = "Bearer $accessToken",
                idFilter = "eq.${item.id}",
                request = UpdateOrderItemQuantityRequest(quantity = quantity)
            )
            _uiState.update { currentState ->
                currentState.copy(
                    cartItems = currentState.cartItems.map {
                        if (it.id == item.id) it.copy(quantity = quantity) else it
                    },
                    message = if (showSuccessMessage) "Added to cart" else currentState.message,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Failed to update cart") }
        }
    }

    private suspend fun deleteCartItem(item: OrderItem, accessToken: String) {
        try {
            NetworkManager.api.deleteCartItem(
                apiKey = SupabaseConfig.ANON_KEY,
                authorization = "Bearer $accessToken",
                idFilter = "eq.${item.id}"
            )
            _uiState.update { currentState ->
                currentState.copy(
                    cartItems = currentState.cartItems.filterNot { it.id == item.id },
                    cartSelections = currentState.cartSelections - item.productId,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Failed to remove cart item") }
        }
    }

    private suspend fun hydrateCartProducts(items: List<OrderItem>, accessToken: String): List<OrderItem> {
        val missingProductIds = items
            .filter { it.products == null && it.productId !in productCache }
            .map { it.productId }
            .distinct()

        missingProductIds.forEach { productId ->
            runCatching {
                NetworkManager.api.getProductById(
                    apiKey = SupabaseConfig.ANON_KEY,
                    authorization = "Bearer $accessToken",
                    idFilter = "eq.$productId"
                ).firstOrNull()
            }.getOrNull()?.let { product ->
                productCache[productId] = product
            }
        }

        return items.map { item ->
            val product = item.products ?: productCache[item.productId]
            if (product == null) item else item.copy(products = product)
        }
    }
}
