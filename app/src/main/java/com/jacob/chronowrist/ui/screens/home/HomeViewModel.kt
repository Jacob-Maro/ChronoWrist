package com.jacob.chronowrist.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.chronowrist.data.model.OrderModel
import com.jacob.chronowrist.data.model.UserModel
import com.jacob.chronowrist.data.model.Watch
import com.jacob.chronowrist.data.model.WatchCategory
import com.jacob.chronowrist.data.repository.AuthRepository
import com.jacob.chronowrist.data.repository.WatchRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    
    var userProfile by mutableStateOf<UserModel?>(null)
        private set

    var orders by mutableStateOf<List<OrderModel>>(emptyList())
        private set

    var allWatches by mutableStateOf(WatchRepository.watches)
        private set

    var selectedCategory by mutableStateOf<WatchCategory?>(null)
        private set

    val filteredWatches: List<Watch>
        get() = if (selectedCategory == null) allWatches
        else allWatches.filter { it.category == selectedCategory }

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            userProfile = authRepository.getUserProfile()
            // Here you would also fetch orders from Supabase
        }
    }

    fun selectCategory(category: WatchCategory?) {
        selectedCategory = category
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logoutUser()
            onSuccess()
        }
    }

    // Functionalities to manage watches (for your request on adding/removing)
    fun addWatch(watch: Watch) {
        allWatches = allWatches + watch
    }

    fun removeWatch(watchId: Int) {
        allWatches = allWatches.filter { it.id != watchId }
    }
}
