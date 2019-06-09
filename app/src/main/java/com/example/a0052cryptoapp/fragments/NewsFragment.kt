package com.example.a0052cryptoapp.fragments

import android.arch.persistence.room.Room
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a0052cryptoapp.adapters.NewsAdapter
import com.example.a0052cryptoapp.R
import com.example.a0052cryptoapp.database.CryptoDatabase
import com.example.a0052cryptoapp.models.CryptoNewsModel
import com.example.a0052cryptoapp.models.NewsData
import com.google.gson.Gson
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.news_fragment.*
import okhttp3.*
import java.io.IOException
import java.util.*

class NewsFragment : Fragment(), NewsAdapter.NewsHolder.OnNewsListener
{
    private val dbNews by lazy {
        Room.databaseBuilder(requireContext(), CryptoDatabase::class.java, "newsDb")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private lateinit var apiKey:String
    private val baseUrl = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN&api_key="
    private var alCryptoNews = arrayListOf<NewsData>()
    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable
    private lateinit var mRandom: Random
    private lateinit var rvNews:RecyclerView
    lateinit var newsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        alCryptoNews.addAll(dbNews.cryptoDao().loadAllNews())
        Log.d("onCreate:", "alCryptoFromDb:$alCryptoNews")
        newsAdapter = NewsAdapter(alCryptoNews, this)
//        rvNews.adapter = newsAdapter
        requireActivity().runOnUiThread{
            newsAdapter.notifyDataSetChanged()
        }
        apiKey = getString(R.string.api_key)
        makeNetworkCall(baseUrl+apiKey)
    }
    private fun makeNetworkCall(url:String)
    {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("onFailure: ", "Failed")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()
                val strJsonResponse = responseBody?.string()
                Log.d("OnResponse: ", strJsonResponse)
                jsonParsingCryptoNews(strJsonResponse)
            }
        })
    }

    fun jsonParsingCryptoNews(strJsonResponse:String?)
    {
        val gson = Gson()
        val cryptoNewsModel = gson.fromJson(strJsonResponse, CryptoNewsModel::class.java)
        Log.d("jsonParsing", cryptoNewsModel.Data[0].title)
        alCryptoNews.clear()
        dbNews.cryptoDao().deleteAllNews()
        alCryptoNews.addAll(cryptoNewsModel.Data)
        Log.d("jsonParsing:", "alCryptoGson:$alCryptoNews")
        if(isAdded)
        {
            requireActivity().runOnUiThread{
                newsAdapter.notifyDataSetChanged()
            }
        }
        dbNews.cryptoDao().insertAllNews(alCryptoNews)
        Log.d("After json Parsing: ", "Size of ArrayList - ${alCryptoNews.size}")
        if(isAdded)
        {
            requireActivity().runOnUiThread{
                rvNews.adapter = newsAdapter
            }
        }
//        Log.d("After json Parsing: ", "close = ${alHistorical[0].close}")
//        Log.d("After json Parsing: ", "close = ${alHistorical[1].close}")
//        Log.d("After json Parsing: ", "close = ${alHistorical[2].close}")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.news_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvNews = view.findViewById(R.id.recycler_view_news_fragment)
        newsAdapter = NewsAdapter(alCryptoNews, this)
        rvNews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvNews.adapter = newsAdapter
        swipeRefreshNews.setOnRefreshListener{
            mHandler = Handler()
            mRandom = Random()
            mRunnable = Runnable{
                makeNetworkCall(baseUrl+apiKey)
                swipeRefreshNews.isRefreshing = false
                rvNews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            mHandler.postDelayed(
                mRunnable,
                500)
        }
    }
    override fun onNewsClick(position: Int)
    {
        val url = alCryptoNews[position].url
        CustomTabsIntent.Builder().build().launchUrl(requireContext(), Uri.parse(url))
    }
}