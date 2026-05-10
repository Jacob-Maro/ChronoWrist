package com.jacob.chronowrist.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.jacob.chronowrist.data.model.CartItem
import com.jacob.chronowrist.data.model.Watch

class CartViewModel : ViewModel() {

    val cartItems = mutableStateListOf<CartItem>()

    val itemCount: Int get() = cartItems.sumOf { it.quantity }

    val subtotal: Double get() = cartItems.sumOf { it.watch.price * it.quantity }

    val shipping: Double get() = if (subtotal >= 500.0) 0.0 else 15.0

    val tax: Double get() = subtotal * 0.08

    val total: Double get() = subtotal + shipping + tax

    fun addToCart(watch: Watch) {
        val existing = cartItems.indexOfFirst { it.watch.id == watch.id }
        if (existing >= 0) {
            cartItems[existing] = cartItems[existing].copy(quantity = cartItems[existing].quantity + 1)
        } else {
            cartItems.add(CartItem(watch = watch, quantity = 1))
        }
    }

    fun incrementQty(watchId: Int) {
        val idx = cartItems.indexOfFirst { it.watch.id == watchId }
        if (idx >= 0) cartItems[idx] = cartItems[idx].copy(quantity = cartItems[idx].quantity + 1)
    }

    fun decrementQty(watchId: Int) {
        val idx = cartItems.indexOfFirst { it.watch.id == watchId }
        if (idx < 0) return
        if (cartItems[idx].quantity <= 1) {
            cartItems.removeAt(idx)
        } else {
            cartItems[idx] = cartItems[idx].copy(quantity = cartItems[idx].quantity - 1)
        }
    }

    fun removeItem(watchId: Int) {
        cartItems.removeAll { it.watch.id == watchId }
    }

    fun clearCart() {
        cartItems.clear()
    }
}
