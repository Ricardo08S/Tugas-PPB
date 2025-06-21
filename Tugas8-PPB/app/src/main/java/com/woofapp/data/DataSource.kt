package com.woofapp.data

import com.woofapp.R

object DataSource {
    val dogs: List<Dog> = listOf(
        Dog(1, R.string.dog_name_koda, 2, R.drawable.koda, R.string.dog_desc_koda),
        Dog(2, R.string.dog_name_lola, 16, R.drawable.lola, R.string.dog_desc_lola),
        Dog(3, R.string.dog_name_frankie, 2, R.drawable.frankie, R.string.dog_desc_frankie),
        Dog(4, R.string.dog_name_nox, 8, R.drawable.nox, R.string.dog_desc_nox),
        Dog(5, R.string.dog_name_faye, 8, R.drawable.faye, R.string.dog_desc_faye),
        Dog(6, R.string.dog_name_bella, 14, R.drawable.bella, R.string.dog_desc_bella),
        Dog(7, R.string.dog_name_moana, 2, R.drawable.moana, R.string.dog_desc_moana),
        Dog(8, R.string.dog_name_tzeitel, 7, R.drawable.tzeitel, R.string.dog_desc_tzeitel)
    )
}