package com.example.a0052cryptoapp.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.a0052cryptoapp.models.CryptoConverterModel
import com.example.a0052cryptoapp.models.HistoricalData
import com.example.a0052cryptoapp.models.InfoData
import com.example.a0052cryptoapp.models.NewsData


@Database(entities = [InfoData::class, NewsData::class ,HistoricalData::class /*, CryptoConverterModel::class*/], version = 1)
abstract class CryptoDatabase : RoomDatabase()
{
    abstract fun cryptoDao(): CryptoDao
}