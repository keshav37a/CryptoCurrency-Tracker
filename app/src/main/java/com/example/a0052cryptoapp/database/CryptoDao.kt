package com.example.a0052cryptoapp.database

import android.arch.persistence.room.*
import com.example.a0052cryptoapp.models.*

@Dao
interface CryptoDao
{
    @Insert
    fun insertAllCurrencies(alInfo: ArrayList<InfoData>) : List<Long>

    @Query("SELECT * FROM info_data_table")
    fun loadAllCurrencies(): List<InfoData>

    @Query("DELETE FROM info_data_table")
    fun deleteAllCurrencies(): Int

    @Insert
    fun insertAllNews(alNews: ArrayList<NewsData>) : List<Long>

    @Query("SELECT * FROM news_table")
    fun loadAllNews(): List<NewsData>

    @Query("DELETE FROM news_table")
    fun deleteAllNews(): Int

    @Insert
    fun insertHistoricalData(alNewsArticle: ArrayList<HistoricalData>) : List<Long>

    @Query("SELECT * FROM historical_data WHERE timeDuration = :typeOfHistoricalData AND currency = :currency")
    fun loadAllHistoricalDataOfType(typeOfHistoricalData: String, currency:String): List<HistoricalData>

    @Query("DELETE FROM historical_data")
    fun deleteAllHistoricalData(): Int

    @Query("DELETE FROM historical_data WHERE timeDuration = :typeOfHistoricalData AND currency = :currency")
    fun deleteHistoricalDataOfType(typeOfHistoricalData: String, currency:String)

//    @Insert
//    fun insertConverterValues(ConverterObject: PriceClass): List<Long>
//
//    @Delete
//    fun deleteConverterValues(ConverterObject: PriceClass): Int
//
//    @Query("SELECT * FROM converter_table")
//    fun loadAllConverterValues(): ArrayList<PriceClass>
}
