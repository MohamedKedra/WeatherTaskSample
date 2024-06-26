package com.example.weathertasksample.ui.feature

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weathertasksample.R
import com.example.weathertasksample.app.utils.Constant
import com.example.weathertasksample.ui.common.EmptyList
import com.example.weathertasksample.ui.common.Fab
import com.example.weathertasksample.ui.common.GenericAppBar
import com.example.weathertasksample.ui.common.WeatherList
import com.example.weathertasksample.ui.theme.WeatherTaskSampleTheme
import com.example.weathertasksample.ui.viewModel.WeatherViewModel

@Composable
fun WeatherHomeScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
){
    val items = viewModel.getAllWeatherItems().observeAsState()
    val context = LocalContext.current
    val isFahrenheit = remember {
        mutableStateOf(true)
    }
    val temp = remember {
        mutableStateOf("Fahrenheit")
    }

    WeatherTaskSampleTheme {
        WeatherTaskSampleTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                Scaffold(
                    topBar = {
                    GenericAppBar(
                        title = "Weather List",
                        onIconClick = {
                                      isFahrenheit.value = ! isFahrenheit.value
                            temp.value = if (isFahrenheit.value) "Fahrenheit" else "Celsius"
                        },
                        icon = {
                            Text(text = temp.value, color = Color.White, fontSize = 14.sp)
                        },
                        iconState = remember {
                            mutableStateOf(true)
                        }
                    )
                },
                    floatingActionButton = {
                        Fab(
                            contentDescription = "add weather",
                            action = {
                                navController.navigate(Constant.Navigation_Home_Add)
                            },
                            icon = R.drawable.baseline_add_to_photos_24
                        )
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize()
                    ) {
                        val list = items.value
                        if (list != null){
                            WeatherList(items = list,isFahrenheit.value,context)
                        }else{
                            EmptyList()
                        }
                    }
                }
            }
        }
    }
}