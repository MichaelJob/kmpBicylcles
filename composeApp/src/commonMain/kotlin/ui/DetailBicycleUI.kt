package ui

import PermissionCallback
import PermissionStatus
import PermissionType
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bicylces.composeapp.generated.resources.Res
import bicylces.composeapp.generated.resources.defaultbicycle
import createPermissionsManager
import data.Bicycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.BicyclesViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import rememberCameraManager
import rememberGalleryManager


@Composable
fun BicycleDetailPage(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    with(viewModel) {
        //camera/gallery based on medium.com/@qasimnawaz_70901/kotlin-multiplatform-compose-unified-image-capture-and-gallery-picker-with-permission-handling-8a8f8cc9cc82
        val permissionsManager = createPermissionsManager(object : PermissionCallback {
            override fun onPermissionStatus(
                permissionType: PermissionType,
                status: PermissionStatus
            ) {
                when (status) {
                    PermissionStatus.GRANTED -> {
                        when (permissionType) {
                            PermissionType.CAMERA -> launchCamera = true
                            PermissionType.GALLERY -> launchGallery = true
                        }
                    }
                    else -> {
                        permissionRationalDialog = true
                    }
                }
            }
        })

        val cameraManager = rememberCameraManager {
            viewModelScope.launch {
                val bitmap = withContext(Dispatchers.Default) {
                    it?.toImageBitmap()
                }
                newImageBitmap = bitmap
                if (bitmap != null) saveNewImage(bitmap)
            }
        }

        val galleryManager = rememberGalleryManager {
            viewModelScope.launch {
                val bitmap = withContext(Dispatchers.Default) {
                    it?.toImageBitmap()
                }
                newImageBitmap = bitmap
                if (bitmap != null) saveNewImage(bitmap)
            }
        }

        if (imageSourceOptionDialog) {
            ImageSourceOptionDialog(onDismissRequest = {
                imageSourceOptionDialog = false
            }, onGalleryRequest = {
                imageSourceOptionDialog = false
                launchGallery = true
            }, onCameraRequest = {
                imageSourceOptionDialog = false
                launchCamera = true
            })
        }
        if (launchGallery) {
            if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
                galleryManager.launch()
            } else {
                permissionsManager.askPermission(PermissionType.GALLERY)
            }
            launchGallery = false
        }
        if (launchCamera) {
            if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
                cameraManager.launch()
            } else {
                permissionsManager.askPermission(PermissionType.CAMERA)
            }
            launchCamera = false
        }
        if (launchSetting) {
            permissionsManager.launchSettings()
            launchSetting = false
        }
        if (permissionRationalDialog) {
            AlertMessageDialog(title = "Permission Required",
                message = "To add bicycle pictures, please grant this permission. You can manage permissions in your device settings.",
                positiveButtonText = "Settings",
                negativeButtonText = "Cancel",
                onPositiveClick = {
                    permissionRationalDialog = false
                    launchSetting = true

                },
                onNegativeClick = {
                    permissionRationalDialog = false
                })
        }

        Column(
            Modifier.fillMaxSize().padding(horizontal = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth().weight(0.9F)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (uiState.showEdit && uiState.currentBicycle?.bikename?.isNotEmpty() == true) {
                        IconButton(
                            onClick = { imageSourceOptionDialog = true }
                        ) {
                            Icon(Icons.Filled.AddCircle, "add a new picture")
                        }
                    }
                    BicycleImages(viewModel = viewModel)
                }
                Text(
                    text = "Bicycle details:",
                    style = typography.h6,
                    modifier = Modifier.padding(10.dp)
                )
                if (uiState.showEdit) {
                    BicycleEditDetails(viewModel = viewModel)
                } else {
                    BicycleDetails(uiState.currentBicycle!!)
                }
            }
            Row(
                modifier = Modifier.fillMaxSize()
                    .weight(0.1F)
                    .padding(vertical = 4.dp, horizontal = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (uiState.showEdit) {
                    //on Edit Detail View
                    Button(onClick = { viewModel.showEdit(false) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "back")
                    }
                    Button(onClick = { viewModel.remove() }) {
                        Icon(Icons.Filled.Delete, "delete")
                    }
                    Button(onClick = { viewModel.save() }) {
                        Icon(Icons.Default.Check, "save")
                    }
                } else {
                    //on Detail View
                    Button(onClick = { viewModel.showDetail(false) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "back")
                    }
                    Button(onClick = { viewModel.showEdit() }) {
                        Icon(Icons.Filled.Edit, "edit")
                    }
                }
            }
        }
    }
}

@Composable
fun BicycleDetails(currentBicycle: Bicycle) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp).verticalScroll(scrollState),
        content = {
            BicycleDetailRow(label = "Bicycle name:", value = currentBicycle.bikename)
            BicycleDetailRow(label = "Frame #:", value = currentBicycle.framenumber)
            BicycleDetailRow(label = "Category:", value = currentBicycle.category)
            BicycleDetailRow(label = "Year:", value = currentBicycle.year)
            BicycleDetailRow(label = "Price:", value = currentBicycle.price)
            BicycleDetailColumn(label = "Description:", value = currentBicycle.description)
        }
    )
}

@Composable
fun BicycleDetailRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().padding(2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label)
        Text(text = value)
    }
}

@Composable
fun BicycleDetailColumn(label: String, value: String) {
    Column(
        Modifier.fillMaxWidth().padding(2.dp),
    ) {
        Text(label)
        Text(value)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BicycleImages(viewModel: BicyclesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val showEdit = uiState.showEdit
    with(uiState.currentBicycle!!) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().height(250.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            items(imagesBitmaps) {
                Box {
                    Image(
                        bitmap = it.second,
                        contentDescription = it.first,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxWidth(if (showEdit) 0.2F else 1.0F)
                            .aspectRatio(1.0f)
                    )
                    if (showEdit) {
                        IconButton(onClick = { viewModel.deleteImage(it) }) {
                            Icon(Icons.Filled.Close, "delete")
                        }
                    }
                }
            }
            if (imagesBitmaps.isEmpty()) { //show default if no images present
                item {
                    Image(
                        painter = painterResource(Res.drawable.defaultbicycle),
                        contentDescription = "defaultbicycle",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxWidth(if (showEdit) 0.2F else 1.0F)
                            .aspectRatio(1.0f),
                    )
                }
            }
        }
    }
}