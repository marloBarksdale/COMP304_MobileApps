package com.example.lyndenflood_comp304sec001_lab04

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.lyndenflood_comp304sec001_lab04.ui.theme.LyndenFlood_COMP304Sec001_Lab04Theme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val attractionName = intent.getStringExtra("attraction_name") ?: "Location"
        val attractionDescription = intent.getStringExtra("attraction_description") ?: ""
        val attractionAddress = intent.getStringExtra("attraction_address") ?: ""
        val latitude = intent.getDoubleExtra("latitude", 41.3851)
        val longitude = intent.getDoubleExtra("longitude", 2.1734)

        setContent {
            LyndenFlood_COMP304Sec001_Lab04Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MapScreen(
                        attractionName = attractionName,
                        attractionDescription = attractionDescription,
                        attractionAddress = attractionAddress,
                        latitude = latitude,
                        longitude = longitude,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    attractionName: String,
    attractionDescription: String,
    attractionAddress: String,
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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

            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = attractionName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004D98)
                )
                Text(
                    text = "📍 $attractionAddress",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }

        // Map
        if (hasLocationPermission) {
            GoogleMapView(
                latitude = latitude,
                longitude = longitude,
                markerTitle = attractionName,
                markerSnippet = attractionDescription,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else {
            // Permission request UI
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🗺️",
                        fontSize = 60.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Location Permission Required",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "To show the map and your location, please grant location permission.",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF004D98)
                        )
                    ) {
                        Text("Grant Permission")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Attraction info card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = attractionName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = attractionDescription,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                )

                if (attractionAddress.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "📍 $attractionAddress",
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
            }
        }
    }
}

@Composable
fun GoogleMapView(
    latitude: Double,
    longitude: Double,
    markerTitle: String,
    markerSnippet: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                onCreate(Bundle())
                getMapAsync { googleMap ->
                    // Enable location layer if permission granted
                    try {
                        googleMap.isMyLocationEnabled = true
                        googleMap.uiSettings.isMyLocationButtonEnabled = true
                    } catch (e: SecurityException) {
                        // Handle permission not granted
                    }

                    // Set map type
                    googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                    // Enable map controls
                    googleMap.uiSettings.isZoomControlsEnabled = true
                    googleMap.uiSettings.isCompassEnabled = true
                    googleMap.uiSettings.isMapToolbarEnabled = true

                    // Create location
                    val location = LatLng(latitude, longitude)

                    // Add marker
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(markerTitle)
                            .snippet(markerSnippet)
                    )

                    // Move camera to location
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(location, 15f)
                    )
                }
                onResume()
            }
        },
        update = { mapView ->
            mapView.onResume()
        }
    )
}