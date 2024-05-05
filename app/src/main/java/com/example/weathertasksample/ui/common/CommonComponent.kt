package com.example.weathertasksample.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.weathertasksample.app.utils.ImageConverter
import com.example.weathertasksample.data.local.Item
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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
    items: List<Item>
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(items) { index, item ->
            WeatherListItem(index, item)
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun WeatherListItem(index: Int, item: Item) {

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.fillMaxSize().padding(vertical = 8.dp)
    ) {
        val bitmap = ImageConverter.converterStringToBitmap(item.imageUri)
        Row {
            Image(
                bitmap = ImageConverter.converterStringToBitmap(item.imageUri)?.asImageBitmap()!!,
                contentDescription = null,
                modifier = Modifier.height(120.dp).width(120.dp).padding(12.dp)
            )
            Column {
                Text(text = item.temp_fah)
                Text(text = item.city)

            }
        }
    }
    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
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

