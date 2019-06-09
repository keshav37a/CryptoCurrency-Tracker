package com.example.a0052cryptoapp.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

data class CryptoNewsModel(
    val Type: Int,
    val Message : String,
    val Data: ArrayList<NewsData>
)

@Entity(tableName = "news_table")
data class NewsData(
    @PrimaryKey(autoGenerate = true) val idd:Long,
    val id:Long,
    val guid:String,
    val published_on:Long,
    val imageurl:String,
    val title:String,
    val url:String,
    val source:String,
    val body:String,
    val tags:String,
    val categories:String,
    val upvotes:Int,
    val downvotes:Int,
//    val lang:String,
    @Embedded val source_info:SourceInfo
)

data class SourceInfo(
    val name:String,
    val lang:String,
    val img:String
)