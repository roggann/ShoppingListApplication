package com.example.shoppinglistapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglistapplication.ui.theme.ShoppingListApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val viewModel: LocationViewModel = viewModel()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(navController, startDestination = "shoppinglistscreen"){
        composable("shoppinglistscreen"){
            ShoppingListApp(
                locationUtils = locationUtils,
                viewModel = viewModel,
                navController = navController,
                context = context,
                address = viewModel.address.value.firstOrNull()?.formatted_address?:"No Address"
            )
        }

        dialog("locationscreen") { backStack ->
            viewModel.location.value?.let { it1 ->
                LocationSelectionScreen(location = it1, onLocationSelected = { locationData ->
                    viewModel.fetchAddress("${locationData.latitude},${locationData.longitude}")
                    navController.popBackStack()
                })

            }
        }
    }
}