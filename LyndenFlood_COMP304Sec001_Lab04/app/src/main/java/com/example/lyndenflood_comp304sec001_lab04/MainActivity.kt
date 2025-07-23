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

/**
 * Data class representing a category of Barcelona attractions
 * Each category contains multiple attractions that users can explore
 */
data class Category(
    val id: String,
    val title: String,
    val description: String,
    val backgroundColor: Color
)

/**
 * MainActivity - Main entry point for Barcelona Travel Guide
 *
 * Displays four main categories of Barcelona attractions:
 * - FC Barcelona (Camp Nou, Museum, La Masia)
 * - Historic Sites (Sagrada Familia, Park Güell, Gothic Quarter)
 * - Beaches (Barceloneta, Bogatell, Nova Icària)
 * - Markets & Food (La Boqueria, El Born, Gràcia)
 *
 * When a category is selected, navigates to LyndenActivity to show specific attractions
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set up the main UI using Jetpack Compose
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

/**
 * Main composable screen showing Barcelona attraction categories
 * Features a header with app branding and a scrollable list of category cards
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcelonaCategoriesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Define the four main categories with Barcelona-themed colors
    val categories = listOf(
        Category(
            id = "fc_barcelona",
            title = "FC Barcelona",
            description = "Camp Nou, La Masia & FC Barcelona Museum",
            backgroundColor = Color(0xFF004D98) // Official FC Barcelona blue
        ),
        Category(
            id = "historic",
            title = "Historic Sites",
            description = "Sagrada Familia, Park Güell & Gothic Quarter",
            backgroundColor = Color(0xFFDC143C) // Historic red
        ),
        Category(
            id = "beaches",
            title = "Beaches",
            description = "Barceloneta, Bogatell & Nova Icaria",
            backgroundColor = Color(0xFF20B2AA) // Mediterranean blue
        ),
        Category(
            id = "food_markets",
            title = "Markets & Food",
            description = "La Boqueria, El Born Market & Tapas Districts",
            backgroundColor = Color(0xFFFF8C00) // Warm food orange
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // App header with branding
        AppHeaderCard()

        Spacer(modifier = Modifier.height(24.dp))

        // Scrollable list of category cards
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onCategoryClick = { selectedCategory ->
                        // Navigate to second activity (LyndenActivity) with category data
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

/**
 * Header card with app title and subtitle
 * Uses FC Barcelona blue theme for branding consistency
 */
@Composable
fun AppHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF004D98) // FC Barcelona blue
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
            Text(
                text = "⚽ 🏖️ 🏛️ 🍽️",
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * Individual category card component
 * Displays category information with themed colors and handles click events
 *
 * @param category The category data to display
 * @param onCategoryClick Callback function when card is clicked
 */
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
            // Category information
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

            // Category icon
            CategoryIcon(categoryId = category.id)
        }
    }
}

/**
 * Category icon component that displays appropriate emoji for each category
 *
 * @param categoryId The unique identifier for the category
 */
@Composable
fun CategoryIcon(categoryId: String) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (categoryId) {
                "fc_barcelona" -> "⚽"  // Football for FC Barcelona
                "historic" -> "🏛️"     // Classical building for historic sites
                "beaches" -> "🏖️"      // Beach for beaches
                "food_markets" -> "🍽️" // Plate for food and markets
                else -> "📍"           // Default location pin
            },
            fontSize = 30.sp
        )
    }
}

/**
 * Preview composable for development and design testing
 */
@Preview(showBackground = true)
@Composable
fun BarcelonaCategoriesPreview() {
    LyndenFlood_COMP304Sec001_Lab04Theme {
        BarcelonaCategoriesScreen()
    }
}