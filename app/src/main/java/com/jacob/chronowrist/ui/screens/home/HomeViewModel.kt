package com.jacob.chronowrist.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jacob.chronowrist.data.model.Watch
import com.jacob.chronowrist.data.model.WatchCategory
import com.jacob.chronowrist.data.repository.WatchRepository

class HomeViewModel : ViewModel() {

    val allWatches = WatchRepository.watches

    var selectedCategory by mutableStateOf<WatchCategory?>(null)
        private set

    val filteredWatches: List<Watch>
        get() = if (selectedCategory == null) allWatches
        else allWatches.filter { it.category == selectedCategory }

    fun selectCategory(category: WatchCategory?) {
        selectedCategory = category
    }
}
