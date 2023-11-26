package com.xperiencelabs.armenu.model

data class Event(
    val id: Int,
    val venue: String,
    val event:String,
    val eventDesc:String,
    val date:String,
    val price:String,
    val location: String,
    val distance: Double,

//    val bookPrice: String,
//    val bookImage: String //hold id of image placed in resource directory
)