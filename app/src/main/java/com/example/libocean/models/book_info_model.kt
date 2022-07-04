package com.example.libocean.models

import androidx.lifecycle.AbstractSavedStateViewModelFactory

data class BookInfo(
    var name: String,
    var price: Int,
    var imgUrl: String

)