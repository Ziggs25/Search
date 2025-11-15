package com.mrndstvndv.search.model

import android.graphics.Bitmap

data class Item(
    val id: String,
    val label: String,
    val icon: Bitmap? = null
)
