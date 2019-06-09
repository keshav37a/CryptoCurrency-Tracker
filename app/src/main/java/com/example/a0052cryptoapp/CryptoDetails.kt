package com.example.a0052cryptoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.a0052cryptoapp.adapters.ViewPagerAdapter
import com.example.a0052cryptoapp.fragments.GraphFragment
import com.example.a0052cryptoapp.models.InfoData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_crypto_details.*
import java.io.Serializable
import java.util.*

class CryptoDetails : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto_details)
        supportActionBar?.hide()
        //Getting the data-----------------------------------------

        //Problem Encountered - intent is null - soln - Always take value from intent in the onCreate method
        val intent2:Intent = intent
        val bundle2 = intent2.getBundleExtra("Bundle")
        val alCryptoInfoDetails = bundle2.getSerializable("ArrayList") as ArrayList<InfoData>
        val position = intent2.getIntExtra("position", 0)
        Log.d("onCreate:CryptoDetails", "alCryptoInfo length : ${alCryptoInfoDetails.size}")
        Log.d("onCreate:CryptoDetails", "position : $position")
        //Getting the data-----------------------------------------

        val alFragments = arrayListOf(
            GraphFragment.newInstance("1H", alCryptoInfoDetails, position),
            GraphFragment.newInstance("1D", alCryptoInfoDetails, position),
            GraphFragment.newInstance("7D", alCryptoInfoDetails, position),
            GraphFragment.newInstance("30D", alCryptoInfoDetails, position),
            GraphFragment.newInstance("6M", alCryptoInfoDetails, position),
            GraphFragment.newInstance("1Y", alCryptoInfoDetails, position)
        )

        //Sending data to fragment
        val bundle3 = Bundle()
        bundle3.putSerializable("ArrayList", alCryptoInfoDetails as Serializable)
        bundle3.putInt("position", position)
        GraphFragment().apply {
            arguments = bundle3
        }
        //Sending data to fragment

        //Setting up Adapter and ViewPager with tabLayout---------------------------
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, alFragments, alCryptoInfoDetails, position)
        viewPagerDetailsUpper.adapter = viewPagerAdapter
        tabLayoutDetails.setupWithViewPager(viewPagerDetailsUpper)
        //Setting up Adapter and ViewPager with tabLayout----------------------------

        //Setting up imageView and name, fullName in cryptoDetails from the array received
        tvNameDetails.text = alCryptoInfoDetails[position].CoinInfo.Name
        tvFullNameDetails.text = alCryptoInfoDetails[position].CoinInfo.FullName
        val baseImgUrl = "https://www.cryptocompare.com"
        Picasso.get().load("$baseImgUrl${alCryptoInfoDetails[position].CoinInfo.ImageUrl}")
            .into(findViewById<ImageView>(R.id.imgViewLogoDetails))
        tvPriceCryptoDetails.text = "${alCryptoInfoDetails[position].RAW.USD.PRICE.toString()} ${alCryptoInfoDetails[position].RAW.USD.TOSYMBOL}"
        tvFromSymbolCryptoDetails.text = "${alCryptoInfoDetails[position].RAW.USD.FROMSYMBOL}/"
        tvToSymbolCryptoDetails.text = alCryptoInfoDetails[position].RAW.USD.TOSYMBOL
        tvLastUpdatedCryptoDetails.text = timeFunc(alCryptoInfoDetails[position].RAW.USD.LASTUPDATED)
        tvMktCapValueCryptoDetails.text = "${alCryptoInfoDetails[position].RAW.USD.MKTCAP.toString()} ${tvToSymbolCryptoDetails.text}"
        tvVolumeValueCryptoDetails.text = "${alCryptoInfoDetails[position].RAW.USD.VOLUME24HOURTO.toString()} ${tvToSymbolCryptoDetails.text}"
        tvSupplyValueCryptoDetails.text = "${alCryptoInfoDetails[position].RAW.USD.SUPPLY.toString()} ${tvToSymbolCryptoDetails.text}"
        //Setting up imageView and name, fullName in cryptoDetails from the array received

        //OnClickListener for backButton
        btnBack.setOnClickListener {
            finish()
        }
    }
    fun timeFunc(timeValue:Double?):String
    {
        val dayName = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday")
        val valueLong:Long = timeValue!!.toLong()
        val timeInMilli: Date = java.util.Date((valueLong*1000))
        val calendar = Calendar.getInstance()
        calendar.time = timeInMilli
        val day = dayName[calendar.get(Calendar.DAY_OF_MONTH)]
        var hour = calendar.get(Calendar.HOUR_OF_DAY).toString()
        if(hour.toLong()<10)
            hour = "0$hour"
        val minutes = calendar.get(Calendar.MINUTE).toString()
        return "Last Updated: $hour : $minutes"
    }
}
