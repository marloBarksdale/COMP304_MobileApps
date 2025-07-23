package com.example.lyndenflood_comp304sec001_lab04

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lyndenflood_comp304sec001_lab04.ui.theme.LyndenFlood_COMP304Sec001_Lab04Theme

data class Category(
    val id: String,
    val title: String,
    val description: String,
    val backgroundColor: Color
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LyndenFlood_COMP304Sec001_Lab04Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BarcelonaCategoriesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcelonaCategoriesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val categories = listOf(
        Category(
            id = "fc_barcelona",
            title = "FC Barcelona",
            description = "Camp Nou, La Masia & FC Barcelona Museum",
            backgroundColor = Color(0xFF004D98) // Barça Blue
        ),
        Category(
            id = "historic",
            title = "Historic Sites",
            description = "Sagrada Familia, Park Güell & Gothic Quarter",
            backgroundColor = Color(0xFFDC143C) // Barça Red
        ),
        Category(
            id = "beaches",
            title = "Beaches",
            description = "Barceloneta, Bogatell & Nova Icaria",
            backgroundColor = Color(0xFF20B2AA) // Mediterranean Blue
        ),
        Category(
            id = "food_markets",
            title = "Markets & Food",
            description = "La Boqueria, El Born Market & Tapas Districts",
            backgroundColor = Color(0xFFFF8C00) // Warm Orange
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF004D98)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Barcelona Travel Guide",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Discover the Magic of Barcelona",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Categories List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onCategoryClick = { selectedCategory ->
                        val intent = Intent(context, LyndenActivity::class.java)
                        intent.putExtra("category_id", selectedCategory.id)
                        intent.putExtra("category_title", selectedCategory.title)
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onCategoryClick: (Category) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onCategoryClick(category) },
        colors = CardDefaults.cardColors(
            containerColor = category.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.description,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 18.sp
                )
            }

            // Icon placeholder - you can add specific icons later
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📍",
                    fontSize = 30.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarcelonaCategoriesPreview() {
    LyndenFlood_COMP304Sec001_Lab04Theme {
        BarcelonaCategoriesScreen()
    }
}