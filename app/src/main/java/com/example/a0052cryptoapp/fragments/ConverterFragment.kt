package com.example.a0052cryptoapp.fragments

import android.R
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.a0052cryptoapp.database.CryptoDatabase
import com.example.a0052cryptoapp.models.CryptoConverterModel
import com.example.a0052cryptoapp.models.PriceClass
import com.google.gson.Gson
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.converter_fragment.*
import okhttp3.*
import java.io.IOException


class ConverterFragment : Fragment()
{
//    private val dbConverterData by lazy {
//        Room.databaseBuilder(requireContext(),
//            CryptoDatabase::class.java,
//            "newsDb.dbHeadlines")
//            .allowMainThreadQueries()
//            .fallbackToDestructiveMigration()
//            .build()
//    }
    private lateinit var itemFrom: String
    private lateinit var itemTo: String
    var itemToPosition = 0
    var itemFromPosition = 0
    private val url =
        "https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,XRP,ETH,EOS,BCH,LTC,BNB,USDT,XLM,ADA,TRX,HT,XMR,MXM,DASHBSV,IOT,ONT,USD,EUR,INR&tsyms=BTC,XRP,ETH,EOS,BCH,LTC,BNB,USDT,XLM,ADA,TRX,HT,XMR,MXM,DASHBSV,IOT,ONT,USD,EUR,INR"
    private var alConverterData = arrayListOf<PriceClass>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.d("ConverterFragment:", "onCreate called")
//        alConverterData.addAll(dbConverterData.cryptoDao().loadAllConverterValues())
        makeNetworkCall(url)
    }

    private fun makeNetworkCall(url: String) {
        Log.d("Converter Fragment", " makeNetworkCall called")
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("makeNetworkCall:", "onFailure : failed")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("makeNetworkCalled", "onResponse:")
                val responseBody = response.body()
                val strJsonResponse = responseBody?.string()
                Log.d("makeNetworkCalled", "responseBody = $strJsonResponse")
                jsonParsing(strJsonResponse)
            }
        })
    }

    fun jsonParsing(strJsonResponse: String?) {
        val gson = Gson()
        val cryptoConverterModel = gson.fromJson(strJsonResponse, CryptoConverterModel::class.java)
        alConverterData.clear()
//        alConverterData.forEach {
//            dbConverterData.cryptoDao().deleteConverterValues(it)
//        }
        alConverterData.addAll(arrayListOf(cryptoConverterModel.BTC, cryptoConverterModel.XRP,
            cryptoConverterModel.ETH, cryptoConverterModel.EOS, cryptoConverterModel.BCH,
            cryptoConverterModel.LTC, cryptoConverterModel.BNB, cryptoConverterModel.USDT,
            cryptoConverterModel.XLM, cryptoConverterModel.ADA, cryptoConverterModel.TRX,
            cryptoConverterModel.HT, cryptoConverterModel.XMR, cryptoConverterModel.MXM,
            cryptoConverterModel.IOT, cryptoConverterModel.ONT, cryptoConverterModel.USD,
            cryptoConverterModel.EUR, cryptoConverterModel.INR))

//        alConverterData.forEach {
//            dbConverterData.cryptoDao().deleteConverterValues(it)
//        }
//
//        alConverterData.forEach {
//            dbConverterData.cryptoDao().insertConverterValues(it)
//        }

        Log.d("ConverterFragment:", "jsonParsing : $alConverterData")
        Log.d("ConverterFragment:", "jsonParsing : ${alConverterData[0]}")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("ConverterFragment:", "onCreateView called")
        //Problem encountered - couldnt findviewbyid of spinner coz I hadnt inflated the fragment xml file
        return inflater.inflate(com.example.a0052cryptoapp.R.layout.converter_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ConverterFragment:", "onViewCreated called")
        val currencyArray = arrayOf(
            "BTC", "XRP", "ETH", "EOS", "BCH", "LTC", "BNB", "USDT",
            "XLM", "ADA", "TRX", "HT", "XMR", "MXM", "IOT", "ONT", "USD", "EUR", "INR"
        )
        requireActivity().runOnUiThread {
            val adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, currencyArray)
            spinnerConverterFrom.adapter = adapter
            spinnerConverterTo.adapter = adapter

            spinnerConverterFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?)
                {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    itemFrom = parent!!.getItemAtPosition(position).toString()
                    itemFromPosition = position
                    Log.d("ConverterFragment:", "itemFrom : $itemFrom")
                    Log.d("ConverterFragment:", "itemFromPosition : $position")
                }
            }

            spinnerConverterTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
                {
                    itemTo = parent!!.getItemAtPosition(position).toString()
                    itemToPosition = position
                    Log.d("ConverterFragment:", "itemTo : $itemTo")
                    Log.d("ConverterFragment:", "itemToPosition : $position")
                }
            }

        }
        btnConvert.setOnClickListener {
            if(etFrom.text.toString().isEmpty())
                Toast.makeText(requireContext(), "Please Enter the value", Toast.LENGTH_SHORT).show()
            else
            {
                val etValue = (etFrom.text.toString()).toDouble()
                when(itemToPosition)
                {
                    0->etTo.text = (alConverterData[itemFromPosition].BTC * etValue).toString()
                    1->etTo.text = (alConverterData[itemFromPosition].XRP * etValue).toString()
                    2->etTo.text = (alConverterData[itemFromPosition].ETH * etValue).toString()
                    3->etTo.text = (alConverterData[itemFromPosition].EOS * etValue).toString()
                    4->etTo.text = (alConverterData[itemFromPosition].BCH * etValue).toString()
                    5->etTo.text = (alConverterData[itemFromPosition].LTC * etValue).toString()
                    6->etTo.text = (alConverterData[itemFromPosition].BNB * etValue).toString()
                    7->etTo.text = (alConverterData[itemFromPosition].USDT * etValue).toString()
                    8->etTo.text = (alConverterData[itemFromPosition].XLM * etValue).toString()
                    9->etTo.text = (alConverterData[itemFromPosition].ADA * etValue).toString()
                    10->etTo.text = (alConverterData[itemFromPosition].TRX * etValue).toString()
                    11->etTo.text = (alConverterData[itemFromPosition].HT * etValue).toString()
                    12->etTo.text = (alConverterData[itemFromPosition].XMR * etValue).toString()
                    13->etTo.text = (alConverterData[itemFromPosition].MXM * etValue).toString()
                    14->etTo.text = (alConverterData[itemFromPosition].IOT * etValue).toString()
                    15->etTo.text = (alConverterData[itemFromPosition].ONT * etValue).toString()
                    16->etTo.text = (alConverterData[itemFromPosition].USD * etValue).toString()
                    17->etTo.text = (alConverterData[itemFromPosition].EUR * etValue).toString()
                    18->etTo.text = (alConverterData[itemFromPosition].INR * etValue).toString()
                }
            }
        }
    }
}