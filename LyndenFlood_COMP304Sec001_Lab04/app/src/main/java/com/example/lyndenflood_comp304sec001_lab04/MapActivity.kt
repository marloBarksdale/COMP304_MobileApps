package com.example.lyndenflood_comp304sec001_lab04

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.lyndenflood_comp304sec001_lab04.ui.theme.LyndenFlood_COMP304Sec001_Lab04Theme
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*

/**
 * MapActivity - Displays attractions on Google Maps with real-time location tracking
 * and geofencing capabilities for Barcelona travel guide
 */
class MapActivity : ComponentActivity() {

    // Location and geofencing components
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private var routePolyline: Polyline? = null

    // Map and UI state
    private var googleMap: GoogleMap? = null
    private val geofenceRadius = 100f // 100 meters radius for geofences

    companion object {
        private const val TAG = "MapActivity"
        private const val NOTIFICATION_CHANNEL_ID = "barcelona_attractions"
        private const val LOCATION_UPDATE_INTERVAL = 5000L // 5 seconds
        private const val LOCATION_FASTEST_INTERVAL = 2000L // 2 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Extract attraction data from intent
        val attractionName = intent.getStringExtra("attraction_name") ?: "Location"
        val attractionDescription = intent.getStringExtra("attraction_description") ?: ""
        val attractionAddress = intent.getStringExtra("attraction_address") ?: ""
        val latitude = intent.getDoubleExtra("latitude", 41.3851)
        val longitude = intent.getDoubleExtra("longitude", 2.1734)

        // Initialize location services
        initializeLocationServices()

        // Create notification channel for geofencing alerts
        createNotificationChannel()

        setContent {
            LyndenFlood_COMP304Sec001_Lab04Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EnhancedMapScreen(
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

    /**
     * Initialize location client and geofencing services
     */
    private fun initializeLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)

        // Setup location update callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                for (location in result.locations) {
                    updateLocationOnMap(location)
                    checkProximityToAttractions(location)
                }
            }
        }
    }

    /**
     * Create notification channel for geofencing alerts (Android 8.0+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Barcelona Attractions",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for nearby Barcelona attractions"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Update user location on map and track movement
     */
    private fun updateLocationOnMap(location: Location) {
        currentLocation = location
        Log.d(TAG, "Location updated: ${location.latitude}, ${location.longitude}")

        // Update map camera if map is ready
        googleMap?.let { map ->
            val userLatLng = LatLng(location.latitude, location.longitude)
            // Smoothly animate to new position
            map.animateCamera(CameraUpdateFactory.newLatLng(userLatLng))
        }
    }

    /**
     * Enhanced proximity checking with multiple distance ranges and better notifications
     */
    private fun checkProximityToAttractions(userLocation: Location) {
        val attractions = getAllBarcelonaAttractions()

        for (attraction in attractions) {
            val attractionLocation = Location("attraction").apply {
                latitude = attraction.latitude
                longitude = attraction.longitude
            }

            val distance = userLocation.distanceTo(attractionLocation)

            when {
                distance <= 50f -> {
                    sendEnhancedProximityNotification(
                        attraction,
                        "🎯 You're at ${attraction.name}!",
                        "Welcome! Perfect time to explore this amazing place.",
                        distance
                    )
                }
                distance <= 150f -> {
                    sendEnhancedProximityNotification(
                        attraction,
                        "🚶 Very close to ${attraction.name}!",
                        "Just ${distance.toInt()}m away - keep walking!",
                        distance
                    )
                }
                distance <= 300f -> {
                    sendEnhancedProximityNotification(
                        attraction,
                        "👀 Approaching ${attraction.name}",
                        "${distance.toInt()}m away - you'll be there soon!",
                        distance
                    )
                }
            }
        }
    }

    /**
     * Send enhanced proximity notifications with better formatting
     */
    private fun sendEnhancedProximityNotification(
        attraction: Attraction,
        title: String,
        message: String,
        distance: Float
    ) {
        val intent = Intent(this, MapActivity::class.java).apply {
            putExtra("attraction_name", attraction.name)
            putExtra("attraction_description", attraction.description)
            putExtra("attraction_address", attraction.address)
            putExtra("latitude", attraction.latitude)
            putExtra("longitude", attraction.longitude)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, attraction.id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

            .build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(attraction.id.hashCode(), notification)
        }
    }

    /**
     * Show route from user location to destination
     */
    private fun showRouteToLocation(destination: LatLng, destinationName: String) {
        currentLocation?.let { userLocation ->
            val origin = LatLng(userLocation.latitude, userLocation.longitude)

            // Remove existing route
            routePolyline?.remove()

            // Create route polyline
            val polylineOptions = PolylineOptions()
                .add(origin)
                .add(destination)
                .color(Color.BLUE)
                .width(10f)
                .pattern(listOf(
                    Dash(30f),
                    Gap(15f)
                ))

            // Get the stored map reference and add route
            googleMap?.let { map ->
                routePolyline = map.addPolyline(polylineOptions)

                // Calculate and show distance info
                val distance = calculateDistanceKm(origin, destination)
                showRouteInformation(destinationName, distance)

                // Zoom to show both points
                zoomToShowBothPoints(origin, destination, map)
            }
        } ?: run {
            Log.w(TAG, "User location not available for route planning")
        }
    }

    /**
     * Calculate distance between two points in kilometers
     */
    private fun calculateDistanceKm(origin: LatLng, destination: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            origin.latitude, origin.longitude,
            destination.latitude, destination.longitude,
            results
        )
        return results[0] / 1000f // Convert to kilometers
    }

    /**
     * Show route information notification
     */
    private fun showRouteInformation(destinationName: String, distanceKm: Float) {
        val walkingTimeMinutes = (distanceKm * 12).toInt() // Rough estimate: 5 km/h walking speed
        val distanceText = if (distanceKm < 1) {
            "${(distanceKm * 1000).toInt()}m"
        } else {
            "${"%.1f".format(distanceKm)}km"
        }

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentTitle("📍 Route to $destinationName")
            .setContentText("Distance: $distanceText • ~$walkingTimeMinutes min walk")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(7777, notification)
        }
    }

    /**
     * Zoom map to show both origin and destination
     */
    private fun zoomToShowBothPoints(origin: LatLng, destination: LatLng, map: GoogleMap) {
        val boundsBuilder = LatLngBounds.Builder()
        boundsBuilder.include(origin)
        boundsBuilder.include(destination)

        val bounds = boundsBuilder.build()
        val padding = 200 // pixels of padding around the bounds

        try {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        } catch (e: Exception) {
            Log.e(TAG, "Error zooming to route bounds", e)
            // Fallback to just centering on destination
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 15f))
        }
    }

    /**
     * Setup Google Map with enhanced features and event handling
     */
    internal fun setupGoogleMap(
        googleMap: GoogleMap,
        latitude: Double,
        longitude: Double,
        markerTitle: String,
        markerSnippet: String
    ) {
        try {
            // Store map reference for route planning
            this.googleMap = googleMap

            // Enable location layer and controls
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
            }

            // Configure map UI settings
            googleMap.uiSettings.apply {
                isZoomControlsEnabled = true
                isCompassEnabled = true
                isMapToolbarEnabled = true
                isRotateGesturesEnabled = true
                isScrollGesturesEnabled = true
                isTiltGesturesEnabled = true
            }

            // Set map type
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            // Create main attraction marker
            val attractionLocation = LatLng(latitude, longitude)
            googleMap.addMarker(
                MarkerOptions()
                    .position(attractionLocation)
                    .title(markerTitle)
                    .snippet(markerSnippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )

            // Add markers for all Barcelona attractions
            addAllAttractionMarkers(googleMap)

            // Set up map event listeners
            setupMapEventListeners(googleMap)

            // Move camera to attraction with padding
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(attractionLocation, 15f))

            Log.d(TAG, "Map setup completed successfully")

        } catch (e: SecurityException) {
            Log.e(TAG, "Location permission not granted", e)
        }
    }

    /**
     * Add markers for all Barcelona attractions
     */
    private fun addAllAttractionMarkers(googleMap: GoogleMap) {
        val allAttractions = getAllBarcelonaAttractions()

        for (attraction in allAttractions) {
            val markerColor = when {
                attraction.id.contains("camp_nou") || attraction.id.contains("fc_") || attraction.id.contains("la_masia") ->
                    BitmapDescriptorFactory.HUE_BLUE // FC Barcelona blue
                attraction.id.contains("sagrada") || attraction.id.contains("park_guell") || attraction.id.contains("gothic") ->
                    BitmapDescriptorFactory.HUE_RED // Historic red
                attraction.id.contains("beach") || attraction.id.contains("barceloneta") || attraction.id.contains("bogatell") ->
                    BitmapDescriptorFactory.HUE_CYAN // Beach cyan
                else -> BitmapDescriptorFactory.HUE_ORANGE // Food/markets orange
            }

            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(attraction.latitude, attraction.longitude))
                    .title(attraction.name)
                    .snippet(attraction.description)
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
            )
        }
    }

    /**
     * Setup enhanced map event listeners with route planning
     */
  fun setupMapEventListeners(googleMap: GoogleMap) {
        // Handle marker clicks with route visualization
        googleMap.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()

            // Show route to clicked attraction
            showRouteToLocation(marker.position, marker.title ?: "Location")

            Log.d(TAG, "Route shown to: ${marker.title}")
            false // Return false to allow default behavior
        }

        // Handle map clicks - clear route
        googleMap.setOnMapClickListener { latLng ->
            Log.d(TAG, "Map clicked at: ${latLng.latitude}, ${latLng.longitude}")
            // Clear any existing route
            routePolyline?.remove()
            routePolyline = null
        }

        // Handle long press to show route to custom location
        googleMap.setOnMapLongClickListener { latLng ->
            showRouteToLocation(latLng, "Custom Location")
            Log.d(TAG, "Route shown to custom location")
        }

        // Handle info window clicks
        googleMap.setOnInfoWindowClickListener { marker ->
            Log.d(TAG, "Info window clicked for: ${marker.title}")
            showRouteToLocation(marker.position, marker.title ?: "Location")
        }
    }

    /**
     * Start real-time location tracking
     */
    private fun startLocationTracking() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_UPDATE_INTERVAL)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(LOCATION_FASTEST_INTERVAL)
            .setMaxUpdateDelayMillis(LOCATION_UPDATE_INTERVAL * 2)
            .build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
            Log.d(TAG, "Location tracking started")
        }
    }

    /**
     * Stop location tracking to preserve battery
     */
    private fun stopLocationTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d(TAG, "Location tracking stopped")
    }

    override fun onResume() {
        super.onResume()
        startLocationTracking()
    }

    override fun onPause() {
        super.onPause()
        stopLocationTracking()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedMapScreen(
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
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    // Permission launcher for location and notifications
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        hasNotificationPermission = permissions[Manifest.permission.POST_NOTIFICATIONS] == true
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Header with navigation info
        EnhancedMapHeader(
            attractionName = attractionName,
            attractionAddress = attractionAddress,
            onBackClick = { (context as ComponentActivity).finish() }
        )

        // Map display or permission request
        if (hasLocationPermission) {
            EnhancedGoogleMapView(
                latitude = latitude,
                longitude = longitude,
                markerTitle = attractionName,
                markerSnippet = attractionDescription,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        } else {
            // Permission request UI
            PermissionRequestCard(
                onRequestPermissions = {
                    val permissionsToRequest = mutableListOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
                    }

                    permissionLauncher.launch(permissionsToRequest.toTypedArray())
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Enhanced attraction info card with distance
        EnhancedAttractionInfoCard(
            attractionName = attractionName,
            attractionDescription = attractionDescription,
            attractionAddress = attractionAddress
        )
    }
}

@Composable
fun EnhancedMapHeader(
    attractionName: String,
    attractionAddress: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = androidx.compose.ui.graphics.Color(0xFF004D98)
            )
        }

        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(
                text = attractionName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color(0xFF004D98)
            )
            Text(
                text = "📍 $attractionAddress",
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color(0xFF666666)
            )
        }

        // Navigation button (placeholder for route planning)
        IconButton(
            onClick = { /* TODO: Implement route planning */ }
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Navigate",
                tint = androidx.compose.ui.graphics.Color(0xFF004D98)
            )
        }
    }
}

@Composable
fun PermissionRequestCard(onRequestPermissions: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🗺️", fontSize = 60.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Location & Notification Permissions Required",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "To show maps, track your location, and send proximity alerts for Barcelona attractions.",
                fontSize = 14.sp,
                color = androidx.compose.ui.graphics.Color(0xFF666666)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRequestPermissions,
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF004D98))
            ) {
                Text("Grant Permissions")
            }
        }
    }
}

@Composable
fun EnhancedAttractionInfoCard(
    attractionName: String,
    attractionDescription: String,
    attractionAddress: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = attractionName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = attractionDescription,
                fontSize = 14.sp,
                color = androidx.compose.ui.graphics.Color(0xFF666666),
                lineHeight = 20.sp
            )

            if (attractionAddress.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "📍 $attractionAddress",
                    fontSize = 12.sp,
                    color = androidx.compose.ui.graphics.Color(0xFF999999)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🔔 You'll get notified when you're nearby!",
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color(0xFF004D98),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EnhancedGoogleMapView(
    latitude: Double,
    longitude: Double,
    markerTitle: String,
    markerSnippet: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
        }
    }

    // Bind lifecycle
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) = mapView.onStart()
            override fun onResume(owner: LifecycleOwner) = mapView.onResume()
            override fun onPause(owner: LifecycleOwner) = mapView.onPause()
            override fun onStop(owner: LifecycleOwner) = mapView.onStop()
            override fun onDestroy(owner: LifecycleOwner) = mapView.onDestroy()
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            mapView.apply {
                getMapAsync { googleMap ->
                    // Call the setupGoogleMap function from the activity
                    (context as? MapActivity)?.setupGoogleMap(
                        googleMap, latitude, longitude, markerTitle, markerSnippet
                    )
                }
            }
        }
    )
}

/**
 * Get all Barcelona attractions for geofencing and markers
 */
fun getAllBarcelonaAttractions(): List<Attraction> {
    return listOf(
        // FC Barcelona attractions
        Attraction("camp_nou", "Camp Nou", "Home stadium of FC Barcelona", "C. d'Aristides Maillol, 12, 08028 Barcelona, Spain", 41.3809, 2.1228),
        Attraction("fc_museum", "FC Barcelona Museum", "Most visited museum in the city", "Camp Nou, Barcelona, Spain", 41.3809, 2.1228),
        Attraction("la_masia", "La Masia", "FC Barcelona's famous youth academy", "Sant Joan Despí, Barcelona, Spain", 41.3644, 2.0986),

        // Historic sites
        Attraction("sagrada_familia", "Sagrada Familia", "Gaudí's unfinished masterpiece", "C/ de Mallorca, 401, Barcelona, Spain", 41.4036, 2.1744),
        Attraction("park_guell", "Park Güell", "Whimsical park designed by Gaudí", "08024 Barcelona, Spain", 41.4145, 2.1527),
        Attraction("gothic_quarter", "Gothic Quarter", "Medieval heart of Barcelona", "Barri Gòtic, Barcelona, Spain", 41.3828, 2.1764),

        // Beaches
        Attraction("barceloneta", "Barceloneta Beach", "Barcelona's most popular beach", "Platja de la Barceloneta, Barcelona, Spain", 41.3755, 2.1838),
        Attraction("bogatell", "Bogatell Beach", "Modern beach with excellent facilities", "Platja del Bogatell, Barcelona, Spain", 41.3897, 2.2039),
        Attraction("nova_icaria", "Nova Icària Beach", "Family-friendly beach with calm waters", "Platja de Nova Icària, Barcelona, Spain", 41.3872, 2.1971),

        // Food & markets
        Attraction("la_boqueria", "La Boqueria Market", "Famous food market on Las Ramblas", "La Rambla, 91, Barcelona, Spain", 41.3816, 2.1715),
        Attraction("el_born", "El Born Market", "Historic market turned cultural center", "Pl. Comercial, 12, Barcelona, Spain", 41.3858, 2.1835),
        Attraction("gracia_food", "Gràcia Food Scene", "Bohemian neighborhood with authentic tapas", "Gràcia, Barcelona, Spain", 41.4036, 2.1588)
    )
}