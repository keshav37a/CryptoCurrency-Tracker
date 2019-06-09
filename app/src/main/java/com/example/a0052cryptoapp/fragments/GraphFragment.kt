package com.example.a0052cryptoapp.fragments

import android.arch.persistence.room.Room
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.a0052cryptoapp.R
import com.example.a0052cryptoapp.database.CryptoDatabase
import com.example.a0052cryptoapp.models.CryptoHistoricalModel
import com.example.a0052cryptoapp.models.HistoricalData
import com.example.a0052cryptoapp.models.InfoData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.graph_fragment_details.*
import okhttp3.*
import java.io.IOException
import java.util.*
import com.github.mikephil.charting.utils.EntryXComparator
import kotlinx.android.synthetic.main.app_bar_main.*

class GraphFragment : android.support.v4.app.Fragment()
{
    companion object
    {
        fun newInstance(endpoint:String,
                        alCryptoDetails: ArrayList<InfoData>,
                        position: Int):GraphFragment
        {
            return GraphFragment().apply {
                val bundle = Bundle()
                bundle.putString("endpoint", endpoint) ////1H 1D 7D
                bundle.putInt("position", position) //0-99
                bundle.putSerializable("ArrayList", alCryptoDetails) //ArrayList of data
                arguments = bundle
            }
        }
    }

    lateinit var lineDataSet: LineDataSet
    var position = 0
    lateinit var alCryptoInfoDetails: ArrayList<InfoData>
    private var alEntryPoints = arrayListOf<Entry>()
    var alPoints = arrayListOf<Point>()
    private val dbGraphData by lazy {
        Room.databaseBuilder(
            requireContext(),
            CryptoDatabase::class.java,
            "newsDb.dbHeadlines"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    var url1 = ""
    private var alHistorical = arrayListOf<HistoricalData>()
    private lateinit var endpoint:String
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //Getting endpoint(1D, 7D, 24H), position, arrayList from bundle----------------------------
        Log.d("OnCreate:GraphFrag", " Called")
        endpoint = arguments!!.getString("endpoint")
        //Problem encountered - spelling mistake in key so always getting null endpoint
        //Problem encountered - always getting bitcoin graph in all rows. mismatch in key of sending intent and
        //getting intent so always getting default value
        position = arguments!!.getInt("position")
        alCryptoInfoDetails = arguments?.getSerializable("ArrayList") as ArrayList<InfoData>
//        Log.d("onCreate:GraphFrag", "endpoint = $endpoint")
        Log.d("onCreate:GraphFrag", "position = $position")
//        Log.d("onCreate:GraphFrag", "ArrayList = $alCryptoInfoDetails")

        alHistorical.addAll(dbGraphData.cryptoDao().loadAllHistoricalDataOfType(endpoint, alCryptoInfoDetails[position].CoinInfo.Name!!))
//        Log.d("onCreate:GraphFrag", "After db loading ArrayList = $alHistorical")
        Log.d("onCreate:GraphFrag", "After db loading ArrayList = ${alHistorical.size} endpoint = $endpoint")
        //Getting endpoint(1D, 7D, 24H), position, arrayList from bundle----------------------------

        val getCoinNameFromPosition = alCryptoInfoDetails[position].CoinInfo.Name
        Log.d("getCoinName", "FromPosition $getCoinNameFromPosition")
        val apiKey = "c798c0d788e7f27760bc9e4a420428738d8295312412a722574f14ab3da0978d"
        val baseUrl = "https://min-api.cryptocompare.com/data/"

        //test endpoint = https://min-api.cryptocompare.com/data/histoday?fsym=BTC&tsym=USD&aggregate=1&limit=7
        //test actual endpoint = {https://min-api.cryptocompare.com/data/}{histoday?}fsym={BTC}&tsym=USD&aggregate=1&limit=7&{apikey=c798c0d788e7f27760bc9e4a420428738d8295312412a722574f14ab3da0978d}
        //test endpoint = https://min-api.cryptocompare.com/histoday?fsym=

        when(endpoint)
        {
            "1H"->{
                url1 = "${baseUrl}histominute?fsym=$getCoinNameFromPosition&tsym=USD&limit=180&apikey=$apiKey"
                Log.d("onCreate:GraphFrag", "when endpoint = $endpoint")
                Toast.makeText(requireContext(), "$endpoint", Toast.LENGTH_SHORT).show()
            }
            "1D"->{
                url1 = "${baseUrl}histominute?fsym=$getCoinNameFromPosition&tsym=USD&limit=1440&apikey=$apiKey"
                Log.d("onCreate:GraphFrag", "when endpoint = $endpoint")
                Toast.makeText(requireContext(), "$endpoint", Toast.LENGTH_SHORT).show()
            }
            "7D"->{
                url1 = "${baseUrl}histohour?fsym=$getCoinNameFromPosition&tsym=USD&limit=168&apikey=$apiKey"
                Log.d("onCreate:GraphFrag", "when endpoint = $endpoint")
                Toast.makeText(requireContext(), "$endpoint", Toast.LENGTH_SHORT).show()
            }
            "30D"->{
                url1 = "${baseUrl}histohour?fsym=$getCoinNameFromPosition&tsym=USD&limit=720&apikey=$apiKey"
                Log.d("onCreate:GraphFrag", "when endpoint = $endpoint")
                Toast.makeText(requireContext(), "$endpoint", Toast.LENGTH_SHORT).show()
            }
            "6M"->{
                url1 = "${baseUrl}histoday?fsym=$getCoinNameFromPosition&tsym=USD&limit=180&apikey=$apiKey"
                Log.d("onCreate:GraphFrag", "when endpoint = $endpoint")
                Toast.makeText(requireContext(), "$endpoint", Toast.LENGTH_SHORT).show()
            }
            "1Y"->{
                url1 = "${baseUrl}histoday?fsym=$getCoinNameFromPosition&tsym=USD&limit=365&apikey=$apiKey"
                Log.d("onCreate:GraphFrag", "when endpoint = $endpoint")
                Toast.makeText(requireContext(), "$endpoint", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(activity, "invalid", Toast.LENGTH_SHORT).show()
                url1 = "https://min-api.cryptocompare.com/data/histoday?fsym=BTC&tsym=USD&aggregate=1&limit=7&apikey=$apiKey"
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        Log.d("OnCreateView:", " Called")
        alEntryPoints.clear()
        return inflater.inflate(R.layout.graph_fragment_details, container, false)
        //Problem - Tabs not scrolling smoothly coz I did not inflate the fragment with xml
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        //APP was crashing due to duplicate entries in arraylist and difficulty in plotting such points
        //Solved by Clearing all 3 arraylists

        //Still when I switch tabs too fast app crashes with the error linechart must not be null
//        java.lang.IllegalStateException: lineChart must not be null
//        at com.example.a0052cryptoapp.fragments.GraphFragment.graphStuff(GraphFragment.kt:187)
//        at com.example.a0052cryptoapp.fragments.GraphFragment$makeNetworkCallHistorical$1.onResponse(GraphFragment.kt:160)
//        at okhttp3.RealCall$AsyncCall.execute(RealCall.java:174)
//        at okhttp3.internal.NamedRunnable.run(NamedRunnable.java:32)
//        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
//        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
//        at java.lang.Thread.run(Thread.java:764)
        Log.d("onViewCreated:GraphFrag", "After db loading ArrayList = ${alHistorical.size} endpoint = $endpoint")
        graphStuff()
//        alHistorical.clear()
//        alPoints.clear()
//        alEntryPoints.clear()
//        alHistorical.addAll(dbGraphData.cryptoDao().loadAllHistoricalDataOfType(endpoint))
//        Log.d("onViewCreated:", "loadFromDb: alHistorical = ${alHistorical.size} endpoint = $endpoint")
//        if(alHistorical.size>0) {
//            Log.d("onViewCreated:", "loadFromDb: alHistorical = ${alHistorical.size} endpoint = $endpoint")
//            graphStuff()
//        }
//            alHistorical.clear()
//            alPoints.clear()
//            alEntryPoints.clear()
//        }

        makeNetworkCallHistorical(url1)
    }

    private fun makeNetworkCallHistorical(url1:String)
    {

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url1)
            .build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("onFailure: ", "Failed")
//                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()
                val strJsonResponse = responseBody?.string()
                Log.d("GraphFragment: ", strJsonResponse)
                jsonParsingHistorical(strJsonResponse)
                alPoints.clear()
                alEntryPoints.clear()
                graphStuff()
            }
        })
    }
    fun jsonParsingHistorical(strJsonResponse:String?)
    {
        val gson = Gson()
        endpoint = arguments!!.getString("endpoint")
        val cryptoHistoricalModel = gson.fromJson(strJsonResponse, CryptoHistoricalModel::class.java)
        alHistorical.addAll(cryptoHistoricalModel.Data)
        Log.d("jsonParsing ", "Coin Info - ${alCryptoInfoDetails[position].CoinInfo.Name}")
        alHistorical.forEach { it.timeDuration = endpoint }
        alHistorical.forEach { it.currency = alCryptoInfoDetails[position].CoinInfo.Name}
        dbGraphData.cryptoDao().deleteHistoricalDataOfType(endpoint, alCryptoInfoDetails[position].CoinInfo.Name!!)
        dbGraphData.cryptoDao().insertHistoricalData(alHistorical)
        //Problem encountered - java-lang-illegalStateException-fragment-not-attached-to-activity  ....
        // changed getActivity to requireContext() and added a check isAdded() before adapter setting
//        Log.d("jsonParsing", cryptoHistoricalModel.data[0].close.toString())
        //Problem encountered - arrayList not initialised. change lateinit var al:ArrayList<HistoricalData> to
        // var al = arrayListOf(HistoricalData)
        Log.d("After json Parsing: ", " alHistorical - ${alHistorical.size}")
//        Log.d("After json Parsing: ", "close = ${alHistorical[0].close}")
//        Log.d("After json Parsing: ", "close = ${alHistorical[1].close}")
//        Log.d("After json Parsing: ", "close = ${alHistorical[2].close}")
    }
    fun graphStuff()
    {
        alHistorical.forEach{
            alPoints.add(Point(it.time, it.high))}
        //Error encountered - lineChart must not be null when we swipe too fast while data is on
        // solved it by adding a null check
        if(lineChart!=null)
        {
            val xAxis = lineChart.xAxis
            xAxis.valueFormatter = AxisValueFormatter(endpoint)
            for (i in alPoints.indices)
            {
                alEntryPoints.add(Entry(alPoints[i].x.toFloat(), alPoints[i].y.toFloat()))
            }
            Collections.sort(alEntryPoints, EntryXComparator())
            Log.d("graphStuff:", "$endpoint: length alPoints = ${alPoints.size}")
            Log.d("graphStuff:", "$endpoint: length alEntryPoints = ${alEntryPoints.size}")
            lineDataSet = LineDataSet(alEntryPoints, "TEST")
            lineDataSet.color = Color.RED
            lineDataSet.setDrawCircles(false)
            lineDataSet.setDrawValues(false)
            val lineData = LineData(lineDataSet)
            lineChart.data = lineData
//        lineChart.zoomIn()
            lineChart.invalidate()
        }
        alHistorical.clear()
    }
    class Point(
        val x:Long,
        val y:Double
    )
    class AxisValueFormatter(endpoint: String):IAxisValueFormatter
    {
        val endpoint = endpoint
        override fun getFormattedValue(value: Float, axis: AxisBase?): String
        {
            val monthName = arrayOf("January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December")
            val valueLong:Long = value.toLong()
            val timeInMilli:Date = java.util.Date((valueLong*1000))
            val calendar = Calendar.getInstance()
            calendar.time = timeInMilli
            val month = monthName[calendar.get(Calendar.MONTH)]
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            return when(endpoint)
            {
                "1H"-> "$hour:$minutes"
                "1D"-> "$hour:$minutes"
                "7D"-> "$month $day"
                "30D"->"$month $day"
                "6M"->"$month"
                "1Y"->"$month"
                else->"$month $day $hour:$minutes"
            }
        }
    }
}


