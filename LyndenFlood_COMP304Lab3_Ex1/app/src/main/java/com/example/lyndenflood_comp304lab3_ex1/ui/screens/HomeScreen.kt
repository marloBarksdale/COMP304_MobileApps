
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FitLog", style = MaterialTheme.typography.headlineSmall) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_activity") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Activity")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Welcome Back!", style = MaterialTheme.typography.titleLarge)

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("stats") }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.BarChart, contentDescription = "Stats")
                    Text("View Progress Stats", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("tips") }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Lightbulb, contentDescription = "Tips")
                    Text("Health & Workout Tips", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
