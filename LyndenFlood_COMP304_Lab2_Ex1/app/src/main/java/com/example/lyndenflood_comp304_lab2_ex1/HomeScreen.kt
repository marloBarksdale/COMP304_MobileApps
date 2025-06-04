import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.lyndenflood_comp304_lab2_ex1.Habit
import com.example.lyndenflood_comp304_lab2_ex1.HabitViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HabitViewModel = getViewModel(),
    onAddHabitClicked: () -> Unit
) {
    val habits by viewModel.habits.observeAsState(emptyList<Habit>())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Habit Journey") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabitClicked
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Text("Grow your habits here!")
        }
    }
}