package com.mrndstvndv.search.model

import androidx.compose.ui.graphics.painter.Painter

data class Item(
    val id: String,
    val label: String,
    val icon: Painter? = null
)
