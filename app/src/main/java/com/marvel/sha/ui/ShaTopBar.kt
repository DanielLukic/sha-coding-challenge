package com.marvel.sha.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.marvel.sha.R

@Composable @OptIn(ExperimentalMaterial3Api::class)
internal fun ShaTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
) = Surface {
    TopAppBar(
        modifier = modifier
            .statusBarsPadding()
            .background(color = MaterialTheme.colorScheme.surface),
        title = {
            Row {
                IconButton(
                    onBackClick,
                    Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.app_back)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    // As title in TopAppBar has extra inset on the left, need to do this: b/158829169
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    )
}
