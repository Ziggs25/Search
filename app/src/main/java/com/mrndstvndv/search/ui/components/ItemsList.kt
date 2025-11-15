package com.mrndstvndv.search.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class PackageItem(val packageName: String, val label: String)

@Composable
fun ItemsList(
    appItems: List<PackageItem>,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        itemsIndexed(appItems) { index, appItem ->
            val shape = when {
                appItems.size == 1 -> RoundedCornerShape(20.dp)
                index == 0 -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, 5.dp, 5.dp)
                index == appItems.lastIndex -> RoundedCornerShape(5.dp, 5.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
                else -> RoundedCornerShape(5.dp)
            }
            Surface(shape = shape, tonalElevation = 1.dp) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable { onItemClick(appItem.packageName) },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(appItem.label, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}
