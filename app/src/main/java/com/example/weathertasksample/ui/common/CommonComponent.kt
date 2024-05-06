@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weathertasksample.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toFile
import coil.compose.AsyncImage
import com.example.common.ImageConverter
import com.example.weathertasksample.R
import com.example.weathertasksample.data.local.Item
import java.io.File
import kotlin.math.roundToInt

@Composable
fun GenericAppBar(
    title: String,
    onIconClick: (() -> Unit),
    icon: @Composable (() -> Unit),
    iconState: MutableState<Boolean>
) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            IconButton(
                onClick = { if (iconState.value) onIconClick.invoke() },
                content = {
                    icon.invoke()
                }
            )
        }
    )
}

@Composable
fun Fab(contentDescription: String, icon: Int, action: () -> Unit) {
    FloatingActionButton(
        onClick = { action.invoke() },
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

@Composable
fun WeatherList(
    items: List<Item>,
    isFahrenheit:Boolean,
    context: Context
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(items) { index, item ->

            item.apply {

                CompactWeatherCard(
                    nameOfLocation = "$city,$country",
                    shortDescription = statusName,
                    shortDescriptionIcon = statusIcon,
                    weatherInDegrees = if (isFahrenheit)temp_fah else temp_cel.toDouble().roundToInt().toString(),
                    windSpeed = windSpeed.plus("km"),
                    humidity = humidity.plus("%"),
                    image=imageUri,
                    context = context,
                    onClick = {
                    })
            }
        }
    }
}

@Composable
fun CompactWeatherCard(
    nameOfLocation: String,
    shortDescription: String,
    shortDescriptionIcon: String,
    weatherInDegrees: String,
    windSpeed: String,
    humidity: String,
    onClick: () -> Unit,
    image:String,
    context:Context,
    modifier: Modifier = Modifier
) {
    val weatherWithDegreesSuperscript = remember(weatherInDegrees) {
        "$weatherInDegrees°"
    }
    OutlinedCard(modifier = modifier.padding(vertical = 8.dp), onClick = onClick) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    androidx.compose.material3.Text(
                        text = nameOfLocation,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    ShortWeatherDescriptionWithIconRow(
                        shortDescription = shortDescription,
                        iconRes = shortDescriptionIcon
                    )
                }
                androidx.compose.material3.Text(
                    text = weatherWithDegreesSuperscript,
                    style = androidx.compose.material3.MaterialTheme.typography.displaySmall
                )
            }

//            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
//            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(image))
//            val file = File(image)
//            val bitmap = BitmapFactory.decodeFile(file.absolutePath,BitmapFactory.Options())
//            Image(bitmap = bitmap.asImageBitmap(), contentDescription =null, modifier = Modifier.height(100.dp).width(100.dp) )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ExtraDataRow(modifier = modifier, title = "Wind Speed", content = windSpeed)
                ExtraDataRow(modifier = modifier, title = "Humidity", content = humidity)
            }
        }
    }
}

@Composable
fun ExtraDataRow(modifier: Modifier, title: String, content: String) {
    Column(modifier = modifier) {
        androidx.compose.material3.Text(title, fontSize = 22.sp, fontWeight = FontWeight(30))
        androidx.compose.material3.Text(content, fontSize = 18.sp)
    }
}

@Composable
fun ShortWeatherDescriptionWithIconRow(
    shortDescription: String,
    iconRes: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model= iconRes,
            modifier = Modifier.size(24.dp),
            contentDescription = null,
        )
        Text(
            text = shortDescription,
            maxLines = 1,
            style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun EmptyList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Empty List")
    }
}

