package com.example.lyndenflood_comp304lab3_ex1.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lyndenflood_comp304lab3_ex1.BuildConfig
import com.example.lyndenflood_comp304lab3_ex1.model.HealthTip
import com.example.lyndenflood_comp304lab3_ex1.repository.TipsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TipsViewModel : ViewModel() {

    private val repository = TipsRepository()

    private val _tips = MutableStateFlow<List<HealthTip>>(emptyList())
    val tips: StateFlow<List<HealthTip>> = _tips

    init {
        loadTips()
    }

    private fun loadTips() = viewModelScope.launch {
        try {
            Log.d("TipsViewModel", "API Key = ${BuildConfig.API_NINJAS_KEY}")
            val response = repository.fetchTips()
            _tips.value = response
            Log.d("TipsViewModel", "✅ Tips loaded: ${response.size} items")


            response.forEach {
                Log.d("TipsViewModel", "• ${it.name}: ${it.instructions.take(30)}...")
            }
        } catch (e: Exception) {
            Log.e("TipsViewModel", "❌ Failed to load tips: ${e.localizedMessage}")
            _tips.value = listOf(
                HealthTip("Error", "n/a", "n/a", "n/a", e.localizedMessage ?: "Unknown error")
            )
        }
    }
}
