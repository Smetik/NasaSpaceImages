package com.example.nasaspaceimages.presentation.spaceimagedetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.nasaspaceimages.domain.model.SpaceImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon

@Composable
fun SpaceImageDetailScreen(
    image: SpaceImage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text("Назад")
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

        HorizontalDivider()

        Text("NASA ID: ${image.nasaId}")

        if (image.center.isNotBlank()) {
            Text("Центр NASA: ${image.center}")
        }

        if (image.dateCreated.isNotBlank()) {
            Text("Дата создания: ${image.dateCreated}")
        }

        Text("Тип: ${image.type.name}")

        if (image.keywords.isNotEmpty()) {
            Text(
                "Ключевые слова: ${image.keywords.joinToString(", ")}"
            )
        }

        if (image.isFavorite) {
            Text("★ В избранном")
        }

        HorizontalDivider()

        Text(
            text = "Описание",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = image.description.ifBlank {
                "Описание отсутствует"
            }
        )

        if (image.comment.isNotBlank()) {
            HorizontalDivider()

            Text(
                text = "Мой комментарий",
                style = MaterialTheme.typography.titleMedium
            )

            Text(image.comment)
        }
    }
}