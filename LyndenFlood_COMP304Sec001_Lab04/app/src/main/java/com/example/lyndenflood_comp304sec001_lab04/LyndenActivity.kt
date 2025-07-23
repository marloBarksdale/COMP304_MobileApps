package com.example.lyndenflood_comp304sec001_lab04

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lyndenflood_comp304sec001_lab04.ui.theme.LyndenFlood_COMP304Sec001_Lab04Theme

/**
 * Data class representing a Barcelona attraction with location coordinates
 * Used for displaying attraction details and passing data to MapActivity
 */
data class Attraction(
    val id: String,           // Unique identifier for the attraction
    val name: String,         // Display name of the attraction
    val description: String,  // Detailed description for users
    val address: String,      // Physical address in Barcelona
    val latitude: Double,     // GPS latitude coordinate
    val longitude: Double     // GPS longitude coordinate
)

/**
 * LyndenActivity - Second activity showing attractions within a selected category
 *
 * This activity receives category information from MainActivity and displays
 * a list of 3 attractions per category with REAL IMAGES loaded from Unsplash.
 * When an attraction is selected, it navigates to MapActivity to show the location on Google Maps.
 *
 * Features:
 * - Real images for each Barcelona attraction
 * - Themed category colors and layouts
 * - Smooth navigation between screens
 * - Professional card design with image overlays
 *
 * The activity supports all four Barcelona categories:
 * - FC Barcelona: Camp Nou, Museum, La Masia
 * - Historic Sites: Sagrada Familia, Park Güell, Gothic Quarter
 * - Beaches: Barceloneta, Bogatell, Nova Icària
 * - Markets & Food: La Boqueria, El Born, Gràcia
 */
class LyndenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Extract category data passed from MainActivity
        val categoryId = intent.getStringExtra("category_id") ?: "fc_barcelona"
        val categoryTitle = intent.getStringExtra("category_title") ?: "Barcelona Attractions"

        Log.d("LyndenActivity", "Displaying category: $categoryTitle ($categoryId)")

        // Set up the UI using Jetpack Compose
        setContent {
            LyndenFlood_COMP304Sec001_Lab04Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AttractionsScreen(
                        categoryId = categoryId,
                        categoryTitle = categoryTitle,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * Main composable screen for displaying attractions within a category
 * Features a header with back navigation and a scrollable list of attraction cards with real images
 *
 * @param categoryId Unique identifier for the selected category
 * @param categoryTitle Display title for the category
 * @param modifier Compose modifier for styling and layout
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionsScreen(
    categoryId: String,
    categoryTitle: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Get attractions for the selected category
    val attractions = getAttractionsByCategory(categoryId)

    Log.d("AttractionsScreen", "Loaded ${attractions.size} attractions for $categoryId")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with back navigation and category title
        AttractionScreenHeader(
            categoryTitle = categoryTitle,
            attractionCount = attractions.size,
            onBackClick = {
                // Navigate back to MainActivity
                (context as ComponentActivity).finish()
            }
        )

        // Scrollable list of attractions with real images
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(attractions) { attraction ->
                AttractionCardWithRealImage(
                    attraction = attraction,
                    onAttractionClick = { selectedAttraction ->
                        Log.d("AttractionsScreen", "Selected attraction: ${selectedAttraction.name}")

                        // Navigate to MapActivity with attraction details
                        val intent = Intent(context, MapActivity::class.java).apply {
                            putExtra("attraction_name", selectedAttraction.name)
                            putExtra("attraction_description", selectedAttraction.description)
                            putExtra("attraction_address", selectedAttraction.address)
                            putExtra("latitude", selectedAttraction.latitude)
                            putExtra("longitude", selectedAttraction.longitude)
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

/**
 * Enhanced header component with back button, category title, and attraction count
 * Provides consistent navigation experience and category context
 */
@Composable
fun AttractionScreenHeader(
    categoryTitle: String,
    attractionCount: Int,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        // Main header row with back button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back navigation button with Barcelona theme
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to categories",
                    tint = Color(0xFF004D98)  // FC Barcelona blue theme
                )
            }

            // Category title with emphasis
            Text(
                text = categoryTitle,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D98),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Subtitle with attraction count
        Text(
            text = "🗺️ $attractionCount must-visit places in Barcelona",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(start = 56.dp, top = 4.dp)
        )
    }
}

/**
 * Enhanced attraction card component with REAL IMAGES from Unsplash
 * Displays attraction information in an appealing card format with professional image overlays
 *
 * @param attraction The attraction data to display
 * @param onAttractionClick Callback function when card is clicked
 */
@Composable
fun AttractionCardWithRealImage(
    attraction: Attraction,
    onAttractionClick: (Attraction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAttractionClick(attraction) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Real image from Unsplash with overlay
            RealAttractionImageWithOverlay(attraction = attraction)

            // Attraction details section
            AttractionDetailsSection(attraction = attraction)
        }
    }
}

/**
 * Real image component that loads actual photos of Barcelona attractions
 * Uses Unsplash API for high-quality, copyright-free images
 * Features a gradient overlay for text readability
 *
 * @param attraction The attraction to load an image for
 */
@Composable
fun RealAttractionImageWithOverlay(attraction: Attraction) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Slightly taller for better image display
    ) {
        // Load real image from Unsplash
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(getAttractionImageUrl(attraction))
                .crossfade(true) // Smooth fade-in animation
                .build(),
            contentDescription = "Photo of ${attraction.name}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop, // Fill the entire space
            onError = { error ->
                Log.e("ImageLoading", "Failed to load image for ${attraction.name}: ${error.result.throwable}")
            },
            onSuccess = {
                Log.d("ImageLoading", "Successfully loaded image for ${attraction.name}")
            }
        )

        // Gradient overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Attraction name overlay at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = attraction.name,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = getCategoryEmoji(attraction.id),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // "View on Map" indicator
            Text(
                text = "🗺️",
                fontSize = 24.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

/**
 * Attraction details section with description, address, and call-to-action
 * Clean, organized layout with proper spacing and typography
 */
@Composable
fun AttractionDetailsSection(attraction: Attraction) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        // Attraction description
        Text(
            text = attraction.description,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Address with location pin
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "📍",
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 8.dp, top = 1.dp)
            )
            Text(
                text = attraction.address,
                fontSize = 12.sp,
                color = Color(0xFF999999),
                lineHeight = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Call-to-action with visual emphasis
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🗺️ Tap to view location on map",
                fontSize = 13.sp,
                color = Color(0xFF004D98),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Get category-specific emoji for visual categorization
 * Helps users quickly identify the type of attraction
 */
fun getCategoryEmoji(attractionId: String): String {
    return when {
        attractionId.contains("camp_nou") || attractionId.contains("fc_") || attractionId.contains("la_masia") -> "⚽ Football"
        attractionId.contains("sagrada") || attractionId.contains("park_guell") || attractionId.contains("gothic") -> "🏛️ Historic"
        attractionId.contains("beach") || attractionId.contains("barceloneta") || attractionId.contains("bogatell") || attractionId.contains("nova_icaria") -> "🏖️ Beach"
        attractionId.contains("boqueria") || attractionId.contains("born") || attractionId.contains("gracia") -> "🍽️ Food"
        else -> "📍 Attraction"
    }
}

/**
 * Get real image URLs for Barcelona attractions from Unsplash
 * Each attraction has a carefully selected, high-quality image
 * Images are optimized for mobile display (400x300px, cropped)
 *
 * @param attraction The attraction to get an image URL for
 * @return Optimized Unsplash image URL
 */
fun getAttractionImageUrl(attraction: Attraction): String {
    return when (attraction.id) {
        // FC Barcelona attractions - Stadium and football imagery
        "camp_nou" -> "https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=400&h=300&fit=crop&crop=center"
        "fc_museum" -> "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=400&h=300&fit=crop&crop=center"
        "la_masia" -> "https://images.unsplash.com/photo-1560272564-c83b66b1ad12?w=400&h=300&fit=crop&crop=center"

        // Historic sites - Architectural marvels
        "sagrada_familia" -> "https://images.unsplash.com/photo-1539037116277-4db20889f2d4?w=400&h=300&fit=crop&crop=center"
        "park_guell" -> "https://images.unsplash.com/photo-1558642452-9d2a7deb7f62?w=400&h=300&fit=crop&crop=center"
        "gothic_quarter" -> "https://images.unsplash.com/photo-1551622954-5c1ddfe06c90?w=400&h=300&fit=crop&crop=center"

        // Beaches - Mediterranean coastline
        "barceloneta" -> "https://images.unsplash.com/photo-1558642084-fd07fae5282e?w=400&h=300&fit=crop&crop=center"
        "bogatell" -> "https://images.unsplash.com/photo-1572103221985-15c5d2a8ead0?w=400&h=300&fit=crop&crop=center"
        "nova_icaria" -> "https://images.unsplash.com/photo-1542919164-ab3d7293b11c?w=400&h=300&fit=crop&crop=center"

        // Food and markets - Culinary experiences
        "la_boqueria" -> "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400&h=300&fit=crop&crop=center"
        "el_born" -> "https://images.unsplash.com/photo-1551622954-5c1ddfe06c90?w=400&h=300&fit=crop&crop=center"
        "gracia_food" -> "https://images.unsplash.com/photo-1582736924624-d1b7c21b6ad6?w=400&h=300&fit=crop&crop=center"

        // Default fallback - Barcelona cityscape
        else -> "https://images.unsplash.com/photo-1539037116277-4db20889f2d4?w=400&h=300&fit=crop&crop=center"
    }
}

/**
 * Data source function that returns attractions based on category ID
 * Each category contains exactly 3 authentic Barcelona attractions with real coordinates
 * All data is accurate and based on actual Barcelona locations
 *
 * @param categoryId The category identifier to filter attractions
 * @return List of attractions for the specified category
 */
fun getAttractionsByCategory(categoryId: String): List<Attraction> {
    return when (categoryId) {
        // FC Barcelona category - football heritage and culture
        "fc_barcelona" -> listOf(
            Attraction(
                id = "camp_nou",
                name = "Camp Nou",
                description = "Home stadium of FC Barcelona and the largest stadium in Europe with a capacity for 99,354 spectators. Experience the magic where Messi, Ronaldinho, and countless legends have played. Take the stadium tour to walk through the tunnel, visit the pitch, and explore the presidential box.",
                address = "C. d'Aristides Maillol, 12, 08028 Barcelona, Spain",
                latitude = 41.3809,
                longitude = 2.1228
            ),
            Attraction(
                id = "fc_museum",
                name = "FC Barcelona Museum",
                description = "The most visited museum in the city and a shrine to football excellence! Discover the incredible history and achievements of FC Barcelona through interactive exhibits, trophy displays, and memorabilia from over 120 years of football greatness.",
                address = "Camp Nou, C. d'Aristides Maillol, 12, 08028 Barcelona, Spain",
                latitude = 41.3809,
                longitude = 2.1228
            ),
            Attraction(
                id = "la_masia",
                name = "La Masia",
                description = "FC Barcelona's world-famous youth academy, the farmhouse that has produced football legends like Messi, Xavi, Iniesta, and Puyol. Known as the greatest football academy in history, it continues to develop the stars of tomorrow with the Barcelona philosophy of beautiful football.",
                address = "Mas Cobert s/n, 08970 Sant Joan Despí, Barcelona, Spain",
                latitude = 41.3644,
                longitude = 2.0986
            )
        )

        // Historic sites category - architectural and cultural UNESCO treasures
        "historic" -> listOf(
            Attraction(
                id = "sagrada_familia",
                name = "Sagrada Familia",
                description = "Antoni Gaudí's unfinished masterpiece and Barcelona's most iconic landmark. This UNESCO World Heritage Site has been under construction for over 140 years and represents the pinnacle of Catalan Modernism. Marvel at the intricate facades, soaring spires, and breathtaking interior light effects.",
                address = "C/ de Mallorca, 401, 08013 Barcelona, Spain",
                latitude = 41.4036,
                longitude = 2.1744
            ),
            Attraction(
                id = "park_guell",
                name = "Park Güell",
                description = "Gaudí's whimsical wonderland featuring colorful mosaic sculptures, unique architectural elements, and stunning panoramic views of Barcelona. Originally designed as a housing development, this UNESCO site is now a magical park where art, nature, and architecture blend in perfect harmony.",
                address = "08024 Barcelona, Spain",
                latitude = 41.4145,
                longitude = 2.1527
            ),
            Attraction(
                id = "gothic_quarter",
                name = "Gothic Quarter (Barri Gòtic)",
                description = "The medieval heart of Barcelona with narrow cobblestone streets, hidden squares, and centuries of history around every corner. Discover ancient Roman walls, Gothic cathedrals, charming cafes, and artisan shops in this perfectly preserved neighborhood that dates back 2,000 years.",
                address = "Barri Gòtic, Barcelona, Spain",
                latitude = 41.3828,
                longitude = 2.1764
            )
        )

        // Beaches category - Mediterranean paradise
        "beaches" -> listOf(
            Attraction(
                id = "barceloneta",
                name = "Barceloneta Beach",
                description = "Barcelona's most famous and accessible beach, perfect for sunbathing, swimming in the Mediterranean, and enjoying fresh seafood at traditional chiringuitos (beach bars). This golden sand beach stretches for 1.25km and offers beach volleyball, water sports, and vibrant nightlife.",
                address = "Platja de la Barceloneta, 08003 Barcelona, Spain",
                latitude = 41.3755,
                longitude = 2.1838
            ),
            Attraction(
                id = "bogatell",
                name = "Bogatell Beach",
                description = "A modern, spacious beach popular with young locals and visitors. Features excellent facilities, beach volleyball courts, outdoor gyms, and a relaxed atmosphere. Less crowded than Barceloneta, it's perfect for those seeking a more authentic Barcelona beach experience with clean sand and clear waters.",
                address = "Platja del Bogatell, 08005 Barcelona, Spain",
                latitude = 41.3897,
                longitude = 2.2039
            ),
            Attraction(
                id = "nova_icaria",
                name = "Nova Icària Beach",
                description = "Family-friendly beach with calm, shallow waters perfect for children. Located near the Olympic Village with nearby parks, playgrounds, and the historic Olympic Port. This beach offers a perfect blend of relaxation and recreation with excellent facilities and stunning Mediterranean views.",
                address = "Platja de Nova Icària, 08005 Barcelona, Spain",
                latitude = 41.3872,
                longitude = 2.1971
            )
        )

        // Food and markets category - culinary paradise
        "food_markets" -> listOf(
            Attraction(
                id = "la_boqueria",
                name = "La Boqueria Market",
                description = "World-famous food market on Las Ramblas, operating since 1217! Experience the vibrant atmosphere while sampling fresh produce, jamón ibérico, local cheeses, tropical fruits, and authentic Spanish tapas. A feast for all senses and the heart of Barcelona's culinary culture.",
                address = "La Rambla, 91, 08001 Barcelona, Spain",
                latitude = 41.3816,
                longitude = 2.1715
            ),
            Attraction(
                id = "el_born",
                name = "El Born Market & District",
                description = "Historic 19th-century cast-iron market hall turned cultural center, surrounded by the trendiest neighborhood in Barcelona. Explore artisan shops, innovative restaurants, cozy wine bars, and vibrant nightlife. The perfect blend of history, culture, and contemporary Barcelona lifestyle.",
                address = "Pl. Comercial, 12, 08003 Barcelona, Spain",
                latitude = 41.3858,
                longitude = 2.1835
            ),
            Attraction(
                id = "gracia_food",
                name = "Gràcia Food Scene",
                description = "Bohemian neighborhood beloved by locals for its authentic tapas bars, family-run restaurants, artisan cafes, and vibrant food markets. Experience the real Barcelona away from tourist crowds - where every corner reveals a new culinary gem and the community spirit is alive and welcoming.",
                address = "Gràcia, Barcelona, Spain",
                latitude = 41.4036,
                longitude = 2.1588
            )
        )

        // Default case - return empty list if category not found
        else -> {
            Log.w("getAttractionsByCategory", "Unknown category: $categoryId")
            emptyList()
        }
    }
}

/**
 * Preview composable for development and design testing
 * Shows the attractions screen with FC Barcelona category data and real images
 */
@Preview(showBackground = true)
@Composable
fun AttractionsWithRealImagesPreview() {
    LyndenFlood_COMP304Sec001_Lab04Theme {
        AttractionsScreen(
            categoryId = "fc_barcelona",
            categoryTitle = "FC Barcelona"
        )
    }
}