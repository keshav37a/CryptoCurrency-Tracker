package com.example.a0052cryptoapp.fragments

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a0052cryptoapp.CryptoDetails
import com.example.a0052cryptoapp.R
import com.example.a0052cryptoapp.adapters.InfoAdapter
import com.example.a0052cryptoapp.database.CryptoDatabase
import com.example.a0052cryptoapp.models.CoinInfo
import com.example.a0052cryptoapp.models.CryptoInfoModel
import com.example.a0052cryptoapp.models.InfoData
import com.google.gson.Gson
import kotlinx.android.synthetic.main.crypto_info_item_row.*
import kotlinx.android.synthetic.main.info_fragment.*
import okhttp3.*
import java.io.IOException
import java.io.Serializable
import java.util.*
import kotlin.Comparator

class InfoFragment : Fragment(), InfoAdapter.InfoViewHolder.OnCryptoListener
{
    private val dbCrypto by lazy {
        Room.databaseBuilder(
            requireContext(),
            CryptoDatabase::class.java,
            "newsDb.dbHeadlines"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private val baseUrl = "https://min-api.cryptocompare.com/data/top/mktcapfull?limit=100&tsym=USD&api_key="
    private lateinit var apiKey:String
    lateinit var rvInfoFrag: RecyclerView
    lateinit var infoAdapter:InfoAdapter
    val alCryptoInfo = arrayListOf<InfoData>()
    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable
    private lateinit var mRandom: Random
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        apiKey = getString(R.string.api_key)
        alCryptoInfo.addAll(dbCrypto.cryptoDao().loadAllCurrencies())
        Log.d("InfoFrag OnCreate ", "after loading from db size = ${alCryptoInfo.size}")
        makeNetworkCall(baseUrl+apiKey)
    }

    private fun makeNetworkCall(url:String)
    {
        Log.d("InfoFragment  ", "makeNetworkCall Called")
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        okHttpClient.newCall(request).enqueue(
            object: Callback
            {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("InfoFragment: ", "Network call Failed")
    //                e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()
                    val strJsonResponse = responseBody?.string()
    //                Log.d("InfoFragment: ", strJsonResponse)
                    jsonParsing(strJsonResponse)
                }
            }
        )
    }

    fun jsonParsing(strJsonResponse:String?)
    {
        val gson = Gson()
        alCryptoInfo.clear()
        if(isAdded)
        {
            requireActivity().runOnUiThread{
                infoAdapter.notifyDataSetChanged()
            }
        }

//        rvInfoFrag.post {
//
//        }
        val cryptoInfoModelObject = gson.fromJson(strJsonResponse, CryptoInfoModel::class.java)
        alCryptoInfo.addAll(cryptoInfoModelObject.Data)
        dbCrypto.cryptoDao().deleteAllCurrencies()
        dbCrypto.cryptoDao().insertAllCurrencies(alCryptoInfo)

        if(isAdded)
        {
            requireActivity().runOnUiThread{
                infoAdapter.notifyDataSetChanged()
            }
        }
//        rvInfoFrag.post {
//            infoAdapter.notifyDataSetChanged()
//        }

        Log.d("After json Parsing: ", "Size of ArrayList - ${alCryptoInfo.size}")
        Log.d("After json Parsing: ", alCryptoInfo[0].CoinInfo.FullName)
        Log.d("After json Parsing: ", alCryptoInfo[1].CoinInfo.FullName)
        Log.d("After json Parsing: ", alCryptoInfo[2].CoinInfo.FullName)
        if(isAdded)
        {
            requireActivity().runOnUiThread{
                rvInfoFrag.adapter = infoAdapter
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        Log.d("onCreateView:", " Called")
        return inflater.inflate(R.layout.info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreated:", " Called")
        getCurrencyFunc()
        infoAdapter = InfoAdapter(alCryptoInfo, this)
        rvInfoFrag = view.findViewById(R.id.recycler_view_info_fragment) as RecyclerView
        rvInfoFrag.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvInfoFrag.adapter = infoAdapter
        mHandler = Handler()
        mRandom = Random()

        swipeRefreshInfo.setOnRefreshListener{
            mRunnable = Runnable{
                makeNetworkCall(baseUrl+apiKey)
                swipeRefreshInfo.isRefreshing = false
                rvInfoFrag.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            mHandler.postDelayed(
                mRunnable,
                500)
        }
    }
    override fun onCurrencyClick(position: Int)
    {
        Log.d("Log", "onCurrencyClicked at position $position")
        //sending data to cryptoDetails Activity and starting activity
        val intent = Intent(requireContext(), CryptoDetails::class.java)
        intent.putExtra("position", position)
        val bundle = Bundle()
        bundle.putSerializable("ArrayList", alCryptoInfo as Serializable)
        intent.putExtra("Bundle", bundle)
        Log.d("onCurrencyClick ", "alCryptoInfo size = ${alCryptoInfo.size}")
        Log.d("onCurrencyClicked:", " $position")
        startActivity(intent)
    }
    fun getCurrencyFunc()
    {
        var alCurrencyList = arrayListOf<String>()
        //apicall = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,XRP,ETH,EOS,BCH,LTC,BNB,USDT,XLM,ADA,TRX,HT,XMR,MXM,DASH,BSV,IOT,ONT,NEO,BAT,ETC,MKR,XEM,KBC,LINK,BEC,XET,ZEC,ORBS,QKC,VET,DOGE,ZRX,BTG,IOST,QTUM,ZIL,OMG,WAVES,RED,USDC,DCR,BCD,USD&tsyms=BTC,XRP,ETH,EOS,BCH,LTC,BNB,USDT,XLM,ADA,TRX,HT,XMR,MXM,DASHBSV,IOT,ONT,USD"
        alCryptoInfo.forEach { alCurrencyList.add(it.CoinInfo.Name!!)}
        Log.d("getCurrencyFunc:", "${alCurrencyList.toString().replace(" ", "")}")
    }
}