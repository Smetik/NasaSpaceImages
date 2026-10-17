package com.example.nasaspaceimages.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.nasaspaceimages.presentation.searchspaceimages.SearchSpaceImagesScreen
import com.example.nasaspaceimages.presentation.spaceimageslist.SpaceImageListScreen
import com.example.nasaspaceimages.presentation.theme.NASASpaceImagesTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.presentation.spaceimagedetail.SpaceImageDetailScreen
import androidx.compose.foundation.lazy.rememberLazyListState


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NASASpaceImagesTheme {

                var selectedTab by rememberSaveable {
                    mutableIntStateOf(0)
                }

                var selectedImage by remember {
                    mutableStateOf<SpaceImage?>(null)
                }

                val searchListState = rememberLazyListState()
                val catalogListState = rememberLazyListState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    bottomBar = {
                        if (selectedImage == null) {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    icon = {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text("Поиск NASA") }
                                )

                                NavigationBarItem(
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
                                    icon = {
                                        Icon(
                                            Icons.Default.List,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text("Мой каталог") }
                                )
                            }
                        }
                    }
                ) { innerPadding ->

                    val image = selectedImage

                    if (image != null) {
                        SpaceImageDetailScreen(
                            image = image,
                            onBack = {
                                selectedImage = null
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        when (selectedTab) {
                            0 -> SearchSpaceImagesScreen(
                                onImageClick = { selectedImage = it },
                                listState = searchListState,
                                modifier = Modifier.padding(innerPadding)
                            )

                            1 -> SpaceImageListScreen(
                                onImageClick = { selectedImage = it },
                                listState = catalogListState,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}