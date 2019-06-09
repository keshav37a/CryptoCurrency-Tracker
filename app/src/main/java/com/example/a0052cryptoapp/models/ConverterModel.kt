package com.example.a0052cryptoapp.models

data class CryptoConverterModel(
     val BTC:PriceClass,
     var XRP:PriceClass,
     var ETH:PriceClass,
     var EOS:PriceClass,
     var BCH:PriceClass,
     var LTC:PriceClass,
     var BNB:PriceClass,
     var USDT:PriceClass,
     var XLM:PriceClass,
     var ADA:PriceClass,
     var TRX:PriceClass,
     var HT:PriceClass,
     var XMR:PriceClass,
     var MXM:PriceClass,
     var IOT:PriceClass,
     var ONT:PriceClass,
     var USD:PriceClass,
     var EUR:PriceClass,
     var INR:PriceClass
)

data class PriceClass(
     var BTC:Double,
     var XRP:Double,
     var ETH:Double,
     var EOS:Double,
     var BCH:Double,
     var LTC:Double,
     var BNB:Double,
     var USDT:Double,
     var XLM:Double,
     var ADA:Double,
     var TRX:Double,
     var HT:Double,
     var XMR:Double,
     var MXM:Double,
     var IOT:Double,
     var ONT:Double,
     var USD:Double,
     var EUR:Double,
     var INR:Double
)

//Problem encountered - error: Cannot figure out how to save this field into database.
// You can consider adding a type converter for it.
//    private com.example.a0052cryptoapp.models.PriceClass EOS;
//Forgot to add embedded on the object vars
