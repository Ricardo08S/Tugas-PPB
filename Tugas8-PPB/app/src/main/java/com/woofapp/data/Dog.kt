package com.woofapp.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Dog(
    val id: Int,
    @StringRes val nameRes: Int,
    val age: Int,
    @DrawableRes val imageResourceId: Int,
    @StringRes val descriptionRes: Int
)