package com.example.weathertasksample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathertasksample.app.utils.Constant
import com.example.weathertasksample.ui.feature.AddNewWeatherScreen
import com.example.weathertasksample.ui.feature.WeatherHomeScreen
import com.example.weathertasksample.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var weatherViewModel : WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            weatherViewModel = hiltViewModel()
            NavHost(navController = navController, startDestination = Constant.Navigation_List){
                composable(Constant.Navigation_List){
                    WeatherHomeScreen(navController = navController, viewModel = weatherViewModel)
                }

                composable(Constant.Navigation_Home_Add){
                    AddNewWeatherScreen(navController = navController, viewModel = weatherViewModel,owner= this@MainActivity)
                }
            }
        }
    }
}