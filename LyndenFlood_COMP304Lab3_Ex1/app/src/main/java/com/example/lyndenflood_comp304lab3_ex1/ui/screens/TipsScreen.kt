package com.example.lyndenflood_comp304lab3_ex1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lyndenflood_comp304lab3_ex1.viewModel.TipsViewModel
import androidx.compose.runtime.getValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen(
    navController: NavHostController,
    viewModel: TipsViewModel = viewModel() // <- this creates and hooks it
) {
    val tips by viewModel.tips.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Health Tips") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tips) { tip ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(tip.name, style = MaterialTheme.typography.titleMedium)
                        Text(tip.instructions, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}



