package com.stoe.wallpapers

import java.sql.Timestamp
import java.util.Date

//4
//cretate a data class to hold the object of a wallpaper item
data class WallpapersModel(
    val name: String = "",
    val image: String = "",  //imageurl
    val thumbnail: String = "",  //neaparat sa initializam cu "" ca e required pt firebase
    val date: Date? = null
)