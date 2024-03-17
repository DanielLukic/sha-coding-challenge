package com.marvel.sha.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
internal fun IndeterminateProgress() = LinearProgressIndicator(
    modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth()
        .height(16.dp)
        .clip(RoundedCornerShape(8.dp)),
)
