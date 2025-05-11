package com.hse.recycleapp.ui.ml

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.scale


@Composable
fun WasteClassifierScreen(navController: NavHostController) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            imageUri = it
        }
    )
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28)
                    bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmap = ImageDecoder.decodeBitmap(
                        source,
                        ImageDecoder.OnHeaderDecodedListener { decoder, info, source ->
                            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                            decoder.isMutableRequired = true
                        })
                }
            }

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Image from the gallery",
                    Modifier.size(400.dp)
                )
                Spacer(modifier = Modifier.padding(20.dp))
                val imageSize = 180
                val scaledBitmap = it.scale(imageSize, imageSize, false);
                TensorFLowHelper.ClassifyImage(scaledBitmap) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(text = "Image is classified as:")
                        Text(text = it, color = Color.Black, fontSize = 24.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.padding(20.dp))

            Button(onClick = {
                launcher.launch("image/*")
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Pick an image")
            }

            Spacer(modifier = Modifier.padding(20.dp))

            Button(
                onClick = { navController.navigate("map") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open map")
            }
        }
    }
}