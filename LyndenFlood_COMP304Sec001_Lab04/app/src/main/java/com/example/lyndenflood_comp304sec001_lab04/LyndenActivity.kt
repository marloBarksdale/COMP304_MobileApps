package com.example.lyndenflood_comp304sec001_lab04

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lyndenflood_comp304sec001_lab04.ui.theme.LyndenFlood_COMP304Sec001_Lab04Theme

data class Attraction(
    val id: String,
    val name: String,
    val description: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

class LyndenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val categoryId = intent.getStringExtra("category_id") ?: "fc_barcelona"
        val categoryTitle = intent.getStringExtra("category_title") ?: "Barcelona Attractions"

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionsScreen(
    categoryId: String,
    categoryTitle: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val attractions = getAttractionsByCategory(categoryId)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    (context as ComponentActivity).finish()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF004D98)
                )
            }

            Text(
                text = categoryTitle,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D98),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Attractions List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(attractions) { attraction ->
                AttractionCard(
                    attraction = attraction,
                    onAttractionClick = { selectedAttraction ->
                        val intent = Intent(context, MapActivity::class.java)
                        intent.putExtra("attraction_name", selectedAttraction.name)
                        intent.putExtra("attraction_description", selectedAttraction.description)
                        intent.putExtra("attraction_address", selectedAttraction.address)
                        intent.putExtra("latitude", selectedAttraction.latitude)
                        intent.putExtra("longitude", selectedAttraction.longitude)
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun AttractionCard(
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for attraction image
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                ) {
                    Text(
                        text = "📸",
                        fontSize = 40.sp,
                        modifier = Modifier.padding(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = attraction.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = attraction.description,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "📍 ${attraction.address}",
                fontSize = 12.sp,
                color = Color(0xFF999999)
            )
        }
    }
}

fun getAttractionsByCategory(categoryId: String): List<Attraction> {
    return when (categoryId) {
        "fc_barcelona" -> listOf(
            Attraction(
                id = "camp_nou",
                name = "Camp Nou",
                description = "Home stadium of FC Barcelona, the largest stadium in Europe with capacity for 99,354 spectators.",
                address = "C. d'Aristides Maillol, 12, 08028 Barcelona, Spain",
                latitude = 41.3809,
                longitude = 2.1228
            ),
            Attraction(
                id = "fc_museum",
                name = "FC Barcelona Museum",
                description = "The most visited museum in the city! Discover the history and achievements of FC Barcelona.",
                address = "Camp Nou, C. d'Aristides Maillol, 12, 08028 Barcelona, Spain",
                latitude = 41.3809,
                longitude = 2.1228
            ),
            Attraction(
                id = "la_masia",
                name = "La Masia",
                description = "FC Barcelona's famous youth academy, where legends like Messi, Xavi, and Iniesta were trained.",
                address = "Mas Cobert s/n, 08970 Sant Joan Despí, Barcelona, Spain",
                latitude = 41.3644,
                longitude = 2.0986
            )
        )
        "historic" -> listOf(
            Attraction(
                id = "sagrada_familia",
                name = "Sagrada Familia",
                description = "Antoni Gaudí's unfinished masterpiece, a UNESCO World Heritage Site and Barcelona's most famous landmark.",
                address = "C/ de Mallorca, 401, 08013 Barcelona, Spain",
                latitude = 41.4036,
                longitude = 2.1744
            ),
            Attraction(
                id = "park_guell",
                name = "Park Güell",
                description = "Whimsical park designed by Gaudí, featuring colorful mosaics and stunning city views.",
                address = "08024 Barcelona, Spain",
                latitude = 41.4145,
                longitude = 2.1527
            ),
            Attraction(
                id = "gothic_quarter",
                name = "Gothic Quarter",
                description = "Medieval heart of Barcelona with narrow streets, historic buildings, and charming squares.",
                address = "Barri Gòtic, Barcelona, Spain",
                latitude = 41.3828,
                longitude = 2.1764
            )
        )
        "beaches" -> listOf(
            Attraction(
                id = "barceloneta",
                name = "Barceloneta Beach",
                description = "Barcelona's most popular beach, perfect for sunbathing, swimming, and beachside dining.",
                address = "Platja de la Barceloneta, 08003 Barcelona, Spain",
                latitude = 41.3755,
                longitude = 2.1838
            ),
            Attraction(
                id = "bogatell",
                name = "Bogatell Beach",
                description = "Modern beach with excellent facilities, popular among young locals and tourists.",
                address = "Platja del Bogatell, 08005 Barcelona, Spain",
                latitude = 41.3897,
                longitude = 2.2039
            ),
            Attraction(
                id = "nova_icaria",
                name = "Nova Icària Beach",
                description = "Family-friendly beach with calm waters and nearby parks, close to the Olympic Village.",
                address = "Platja de Nova Icària, 08005 Barcelona, Spain",
                latitude = 41.3872,
                longitude = 2.1971
            )
        )
        "food_markets" -> listOf(
            Attraction(
                id = "la_boqueria",
                name = "La Boqueria Market",
                description = "Famous food market on Las Ramblas, offering fresh produce, tapas, and local delicacies.",
                address = "La Rambla, 91, 08001 Barcelona, Spain",
                latitude = 41.3816,
                longitude = 2.1715
            ),
            Attraction(
                id = "el_born",
                name = "El Born Market",
                description = "Historic market turned cultural center, surrounded by trendy restaurants and bars.",
                address = "Pl. Comercial, 12, 08003 Barcelona, Spain",
                latitude = 41.3858,
                longitude = 2.1835
            ),
            Attraction(
                id = "gracia_food",
                name = "Gràcia Food Scene",
                description = "Bohemian neighborhood with authentic tapas bars, local restaurants, and vibrant food culture.",
                address = "Gràcia, Barcelona, Spain",
                latitude = 41.4036,
                longitude = 2.1588
            )
        )
        else -> emptyList()
    }
}

@Preview(showBackground = true)
@Composable
fun AttractionsPreview() {
    LyndenFlood_COMP304Sec001_Lab04Theme {
        AttractionsScreen(
            categoryId = "fc_barcelona",
            categoryTitle = "FC Barcelona"
        )
    }
}