package com.marvel.sha.ui

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.marvel.sha.R

@Composable @OptIn(ExperimentalMaterial3Api::class)
internal fun ShaTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
) = Surface {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        modifier = modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.app_back)
                )
            }
        },
        title = {
            val style = if (title.contains("\n"))
                MaterialTheme.typography.titleSmall
            else
                MaterialTheme.typography.titleLarge
            Text(
                text = title,
                style = style,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )
        }
    )
}
