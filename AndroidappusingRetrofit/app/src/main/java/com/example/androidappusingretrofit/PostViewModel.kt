package com.example.androidappusingretrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    init {
        viewModelScope.launch {
            try {
                _posts.value = RetrofitClient.api.getPosts()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
