package com.mrndstvndv.search.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import com.mrndstvndv.search.model.Item

@Composable
fun ItemsList(
    appItems: List<Item>,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        itemsIndexed(appItems) { index, appItem ->
            val targetTopStart = when {
                appItems.size == 1 -> 20.dp
                index == 0 -> 20.dp
                else -> 5.dp
            }
            val targetTopEnd = when {
                appItems.size == 1 -> 20.dp
                index == 0 -> 20.dp
                else -> 5.dp
            }
            val targetBottomStart = when {
                appItems.size == 1 -> 20.dp
                index == appItems.lastIndex -> 20.dp
                else -> 5.dp
            }
            val targetBottomEnd = when {
                appItems.size == 1 -> 20.dp
                index == appItems.lastIndex -> 20.dp
                else -> 5.dp
            }

            val animatedTopStart by animateDpAsState(targetTopStart, animationSpec = tween(durationMillis = 250))
            val animatedTopEnd by animateDpAsState(targetTopEnd, animationSpec = tween(durationMillis = 250))
            val animatedBottomStart by animateDpAsState(targetBottomStart, animationSpec = tween(durationMillis = 250))
            val animatedBottomEnd by animateDpAsState(targetBottomEnd, animationSpec = tween(durationMillis = 250))

            val shape = RoundedCornerShape(
                topStart = animatedTopStart,
                topEnd = animatedTopEnd,
                bottomEnd = animatedBottomEnd,
                bottomStart = animatedBottomStart,
            )
            Surface(shape = shape, tonalElevation = 1.dp) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable { onItemClick(appItem.id) }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    appItem.icon?.let { bitmap ->
                        val painter = remember(bitmap) { BitmapPainter(bitmap.asImageBitmap()) }
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                    }
                    Text(appItem.label)
                }
            }
        }
    }
}
