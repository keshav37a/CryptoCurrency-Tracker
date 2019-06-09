package com.example.a0052cryptoapp.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

data class CryptoInfoModel(
var Message : String,
var Type: Int,
var Data: ArrayList<InfoData>
):Serializable

@Entity(tableName = "info_data_table")
data class InfoData(
    @PrimaryKey(autoGenerate = true) var unId:Long,
    @Embedded var CoinInfo:CoinInfo,
    @Embedded var RAW:RAW
):Serializable

data class CoinInfo(
    var id:Long?=10000,
    var Name:String?="None",
    var FullName:String?="None",
    var Internal:String?="None",
    var ImageUrl:String?="None",
    var Url:String?="None",
    var Algorithm:String?="None",
    var ProofOfType:String?="None",
    var NetHashesPerSecond:Double?=10000.00,
    var BlockNumber:Double?=10000.00,
    var BlockTime:Double?=10000.00,
    var BlockReward:Double?=10000.00,
    var Type:Int?=10000,
    var DocumentType:String?="None"
):Serializable

data class RAW(
      @Embedded var USD:USD
):Serializable

data class USD(
    var Market:String?="NONE",
    var FROMSYMBOL:String?="None",
    var TOSYMBOL:String?="None",
    var FLAGS:String?="None",
    var PRICE:Double?=10000.00,
    var LASTUPDATED:Double?=10000.00,
    var LASTVOLUME:Double?=10000.00,
    var LASTVOLUMETO:Double?=10000.00,
    var LASTTRADEID:String?="None",
    var VOLUMEDAY:Double?=10000.00,
    var VOLUMEDAYTO:Double?=10000.00,
    var VOLUME24HOUR:Double?=10000.00,
    var VOLUME24HOURTO:Double?=10000.00,
    var OPENDAY:Double?=10000.00,
    var HIGHDAY:Double?=10000.00,
//    var TYPE:Int,
    var LOWDAY:Double?=10000.00,
    var OPEN24HR:Double?=10000.00,
    var HIGH24HR:Double?=10000.00,
    var LOW24HR:Double?=10000.00,
    var LASTMARKET:String?="None",
    var VOLUMEHOUR:Double?=10000.00,
    var VOLUMEHOURTO:Double?=10000.00,
    var OPENHOUR:Double?=10000.00,
    var HIGHHOUR:Double?=10000.00,
//    var IMAGEURL:String,
    var LOWHOUR:Double?=10000.00,
    var CHANGE24HOUR:Double?=10000.00,
    var CHANGEPCT24HOUR:Double?=10000.00,
    var CHANGEDAY:Double?=10000.00,
    var CHANGEPCTDAY:Double?=10000.00,
    var SUPPLY:Double?=10000.00,
    var MKTCAP:Double?=10000.00,
    var TOTALVOLUME24H:Double?=10000.00,
    var TOTALVOLUME24HTO:Double?=10000.00
):Serializable


