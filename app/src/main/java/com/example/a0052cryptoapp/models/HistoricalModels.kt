package com.example.a0052cryptoapp.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

data class CryptoHistoricalModel(
    val Response:String,
    val Type:Int,
    val Aggregated:Boolean,
    val Data:ArrayList<HistoricalData>,
    val TimeTo:Long,
    val TimeFrom: Long
)

@Entity(tableName = "historical_data")
data class HistoricalData(
    @PrimaryKey(autoGenerate = true) val id:Long,
    val time:Long,
    val close: Double,
    val high: Double,
    val low: Double,
    val open:Double,
    val volumefrom:Double,
    val volumeto:Double,
    var timeDuration: String,
    var currency: String?
)
