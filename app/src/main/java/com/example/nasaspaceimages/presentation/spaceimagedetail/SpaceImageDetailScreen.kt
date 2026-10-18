package com.example.nasaspaceimages.presentation.spaceimagedetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.nasaspaceimages.R
import com.example.nasaspaceimages.domain.model.SpaceImage

@Composable
fun SpaceImageDetailScreen(
    image: SpaceImage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpaceImageDetailViewModel = hiltViewModel()
) {
    BackHandler(onBack = onBack)

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val resources = LocalResources.current

    LaunchedEffect(viewModel, resources) {
        viewModel.messages.collect { messageRes ->
            snackbarHostState.showSnackbar(
                resources.getString(messageRes)
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.back))
            }

            Text(
                text = image.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            AsyncImage(
                model = image.mediaUrl,
                contentDescription = image.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                contentScale = ContentScale.Fit
            )

            FilledTonalButton(
                onClick = {
                    viewModel.downloadImage(image)
                }
            ) {
                Icon(
                    Icons.Filled.Download,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.download_original))
            }

            HorizontalDivider()

            Text(
                stringResource(
                    R.string.image_nasa_id,
                    image.nasaId
                )
            )

            if (image.center.isNotBlank()) {
                Text(
                    stringResource(
                        R.string.image_center,
                        image.center
                    )
                )
            }

            if (image.dateCreated.isNotBlank()) {
                Text(
                    stringResource(
                        R.string.image_date,
                        image.dateCreated
                    )
                )
            }

            Text(
                stringResource(
                    R.string.image_type,
                    image.type.name
                )
            )

            if (image.keywords.isNotEmpty()) {
                Text(
                    stringResource(
                        R.string.image_keywords,
                        image.keywords.joinToString(", ")
                    )
                )
            }

            if (image.isFavorite) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.in_favorites))
                }
            }

            HorizontalDivider()

            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleMedium
            )

            val description =
                if (image.description.isBlank()) {
                    stringResource(R.string.description_missing)
                } else {
                    image.description
                }

            Text(description)

            if (image.comment.isNotBlank()) {
                HorizontalDivider()

                Text(
                    text = stringResource(R.string.my_comment),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(image.comment)
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}