package com.example.weathertasksample.ui.feature

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.common.ImageConverter
import com.example.weathertasksample.MainActivity
import com.example.weathertasksample.R
import com.example.common.DataState
import com.example.weathertasksample.app.utils.Constant
import com.example.weathertasksample.data.FileConverter
import com.example.weathertasksample.data.local.BitmapConverter
import com.example.weathertasksample.ui.common.Fab
import com.example.weathertasksample.ui.common.GenericAppBar
import com.example.weathertasksample.ui.theme.WeatherTaskSampleTheme
import com.example.weathertasksample.ui.viewModel.WeatherViewModel
import kotlinx.coroutines.launch

@Composable
fun AddNewWeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel(),
    owner: MainActivity,
    latLng: MutableState<Pair<Double, Double>>
) {
    val city = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }
    val tempFah = remember { mutableStateOf("") }
    val tempCel = remember { mutableStateOf("") }
    val humidity = remember { mutableStateOf("") }
    val windSpeed = remember { mutableStateOf("") }
    val statusName = remember { mutableStateOf("") }
    val statusIcon = remember { mutableStateOf("") }
    val currentPhoto = remember { mutableStateOf("") }
    val savaButtonState = remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val image: Bitmap = BitmapFactory.decodeResource(
        Resources.getSystem(),
        android.R.drawable.ic_menu_report_image
    )
    val bitmap = remember {
        mutableStateOf(image)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        if (it != null) {
            bitmap.value = it
        }
        currentPhoto.value = ImageConverter.converterBitmapToString(it!!)
    }
    val launcherImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        if (Build.VERSION.SDK_INT < 28) {

            bitmap.value =
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    it
                )
            savaButtonState.value = true
        } else {
            val source = it?.let { it1 ->
                ImageDecoder.createSource(context.contentResolver, it1)
            }
            bitmap.value = source?.let { it1 ->
                ImageDecoder.decodeBitmap(it1)
            }!!
            savaButtonState.value = true
        }
        currentPhoto.value = it.toString()
    }

    WeatherTaskSampleTheme {
        LaunchedEffect(key1 = "") {
            viewModel.getCurrentLocationWeather(latLng.value.first.toString(),latLng.value.second.toString())
            viewModel.weatherDetailsResponse.observe(owner) {
                scope.launch {
                    Log.d("Tag", it.toString())
                    when (it?.getStatus()) {
                        DataState.DataStatus.LOADING -> {

                        }

                        DataState.DataStatus.SUCCESS -> {
                            it.getData()?.apply {
                                val fahTemp: Double = main?.temp.toString().toDouble()
                                val celTemp: Double = ((fahTemp - 32) * 5) / 9
                                humidity.value = main?.humidity.toString()
                                windSpeed.value = wind?.speed.toString()
                                tempFah.value = fahTemp.toString()
                                tempCel.value = celTemp.toString()
                                statusName.value = weather?.get(0)?.main.toString()
                                statusIcon.value =
                                    Constant.iconUrl + weather?.get(0)?.icon + Constant.imageSuffix
                                city.value = name.toString()
                                country.value = sys?.country.toString()
                            }
                        }

                        DataState.DataStatus.ERROR -> {
                            snackBarHostState.showSnackbar(it.getError()?.message.toString())

                        }

                        DataState.DataStatus.NO_INTERNET -> {
                            snackBarHostState.showSnackbar("No Internet connection")
                        }

                        else -> {
                            snackBarHostState.showSnackbar("Something wrong")
                        }
                    }
                }
            }
        }
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                },
                topBar = {
                    GenericAppBar(
                        title = "Add New Weather",
                        onIconClick = {
                            if (savaButtonState.value) {
                                viewModel.addNewWeatherItem(
                                    city = city.value,
                                    country = country.value,
                                    tempFah = tempFah.value,
                                    tempCel = tempCel.value,
                                    humidity = humidity.value,
                                    windSpeed = windSpeed.value,
                                    statusName = statusName.value,
                                    statusIcon = statusIcon.value,
                                    imageUri = currentPhoto.value
                                )

                                scope.launch {
                                    snackBarHostState.showSnackbar("Item is added successfully")
                                }
                                navController.popBackStack()
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Add Ur Image")
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_to_photos_24),
                                contentDescription = "save note",
                                tint = Color.White
                            )
                        },
                        iconState = savaButtonState
                    )
                },
                floatingActionButton = {
                    Fab(
                        contentDescription = "add weather",
                        action = {
                            showDialog = true
                        },
                        icon = R.drawable.baseline_camera_alt_24
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    Image(
                        bitmap = bitmap.value.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(250.dp)
                            .background(color = Color.White)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = CircleShape
                            )
                    )
                    val bit = ImageConverter.converterStringToBitmap(currentPhoto.value)
                    if (bit != null) {
                        Image(
                            bitmap = bit.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(250.dp)
                                .background(color = Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = CircleShape
                                )
                        )
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)) {
                        Text(text = city.value.plus(",${country.value}"), color = Color.Black, fontSize = 17.sp)
                        Text(text = "Latitude: ${latLng.value.first} , Longitude: ${latLng.value.second} ", color = Color.Black, fontSize = 15.sp)
                    }

                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 10.dp)
                    ) {
                        if (showDialog) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(100.dp)
                                    .clip(
                                        RoundedCornerShape(10.dp)
                                    )
                                    .background(Color.White)
                            ) {
                                Column(modifier = Modifier.padding(start = 60.dp)) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clickable {
                                                launcher.launch(null)
                                                showDialog = false
                                            }
                                    )
                                    Text(text = "Camera", color = Color.Black)
                                }
                                Spacer(modifier = Modifier.padding(30.dp))
                                Column {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_broken_image_24),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clickable {
                                                launcherImage.launch(arrayOf("image/*"))
                                                showDialog = false
                                            }
                                    )
                                    Text(text = "Gallery", color = Color.Black)
                                }
                                Column(
                                    modifier = Modifier.padding(
                                        start = 50.dp,
                                        bottom = 80.dp
                                    )
                                ) {
                                    Text(
                                        text = "X",
                                        color = Color.Black,
                                        modifier = Modifier.clickable {
                                            showDialog = false
                                        })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}