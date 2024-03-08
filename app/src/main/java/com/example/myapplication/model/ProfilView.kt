package com.example.myapplication.model

import android.graphics.Bitmap

interface ProfilView {
    fun showImage(image: Bitmap)
    fun showError(message: String)
}