package com.example.nasaspaceimages.presentation.spaceimageslist

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nasaspaceimages.R
import com.example.nasaspaceimages.domain.model.SpaceImage
import com.example.nasaspaceimages.presentation.components.SpaceImageCard

@Composable
fun SavedSpaceImageCard(
    image: SpaceImage,
    onOpen: () -> Unit,
    onFavoriteClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    SpaceImageCard(
        image = image,
        onOpen = onOpen,
        modifier = Modifier.padding(
            horizontal = 8.dp,
            vertical = 6.dp
        ),
        trailingActions = {
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (image.isFavorite) {
                        Icons.Filled.Star
                    } else {
                        Icons.Filled.StarBorder
                    },
                    contentDescription = stringResource(
                        if (image.isFavorite) {
                            R.string.favorite_remove
                        } else {
                            R.string.favorite_add
                        }
                    )
                )
            }

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit)
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        },
        bottomContent = {
            if (image.comment.isNotBlank()) {
                Text(
                    text = image.comment,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}

@Composable
fun DeleteSpaceImageDialog(
    image: SpaceImage,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.delete_image_title))
        },
        text = {
            Text(
                stringResource(
                    R.string.delete_image_message,
                    image.title
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun EditSpaceImageDialog(
    image: SpaceImage,
    onSave: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember(image.id) {
        mutableStateOf(image.title)
    }

    var comment by remember(image.id) {
        mutableStateOf(image.comment)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.edit_image_title))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(stringResource(R.string.image_title))
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = {
                        Text(stringResource(R.string.comment))
                    },
                    minLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = title.isNotBlank(),
                onClick = {
                    onSave(title, comment)
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}