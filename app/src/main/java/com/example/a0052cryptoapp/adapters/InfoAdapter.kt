package com.example.a0052cryptoapp.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.a0052cryptoapp.R
import com.example.a0052cryptoapp.models.InfoData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.crypto_info_item_row.view.*

class InfoAdapter(private var alCrypto:ArrayList<InfoData>,
                  var onCryptoListener: InfoViewHolder.OnCryptoListener)
    : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>()
{
    private var marketCap:Double? = 0.0
    private var totalVolume:Double? = 0.0
    private var hChange:Double?= 0.0
    private var oneDChange:Double?= 0.0
    private var fullName:String?=""

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): InfoViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.crypto_info_item_row, parent, false)
        return InfoViewHolder(inflatedView, onCryptoListener)
    }

    override fun getItemCount(): Int {
        return alCrypto.size
    }

    override fun onBindViewHolder(parent: InfoViewHolder, position: Int)
    {
        val baseImgUrl = "https://www.cryptocompare.com"
        Picasso.get().load("$baseImgUrl${alCrypto[position].CoinInfo.ImageUrl}")
            .into(parent.itemView.findViewById<ImageView>(R.id.imgViewLogo))

        hChange =  alCrypto[position].RAW.USD.CHANGEPCT24HOUR
        oneDChange = alCrypto[position].RAW.USD.CHANGEPCTDAY
        marketCap =  alCrypto[position].RAW.USD.MKTCAP
        totalVolume = alCrypto[position].RAW.USD.VOLUME24HOURTO
        fullName = alCrypto[position].CoinInfo.FullName

        var amount = normalizeValue(marketCap, "marketCap")
        parent.itemView.tvMarketCap.text = String.format("%.2f", marketCap) + " $amount  Cap"

        amount = normalizeValue(totalVolume, "totalVolume")
        parent.itemView.tvTotalVolume.text = String.format("%.2f", totalVolume) + " $amount  Vol"

        parent.itemView.tvFullName.text = fullName
        parent.itemView.tvName.text = alCrypto[position].CoinInfo.Name
        parent.itemView.tvPrice.text = "$" + String.format("%.2f", alCrypto[position].RAW.USD.PRICE)
        parent.itemView.tv24HChange.text = String.format("%.2f", hChange)+"%  1H"
        parent.itemView.tv1DChange.text = String.format("%.2f", oneDChange)+"%  1D"



        if(hChange!!>0)
            parent.itemView.tv24HChange.setTextColor(Color.GREEN)
        else
            parent.itemView.tv24HChange.setTextColor(Color.RED)

        if(oneDChange!!>0)
            parent.itemView.tv1DChange.setTextColor(Color.GREEN)
        else
            parent.itemView.tv1DChange.setTextColor(Color.RED)

    }

    private fun normalizeValue(value:Double?, string:String):String
    {
        var value2 = value
        var count = 0
        var amount = ""
        while(value2!!>100)
        {
            value2/=10
            count++
        }
        Log.d("count value ", "$fullName = $count volume = $totalVolume")
        when(count)
        {
            10->{
                amount = "B"
                value2 *= 10
                }
            9->amount = "B"
            8->{
                amount = "B"
                value2/=10
            }
            7->{
                amount = "M"
                value2*=10
            }
            6->amount = "M"
            5->{
                amount = "M"
                value2/=10
            }
            4->{
                amount = "K"
                value2 *= 10
            }
            3->amount = "K"


            else->"?"
        }
        if(string == "marketCap")
            marketCap = value2
        else if(string == "totalVolume")
            totalVolume = value2
        return amount
    }
    class InfoViewHolder(itemView: View, onCryptoListener: OnCryptoListener)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        private var onCryptoListener: OnCryptoListener

        init {
            itemView.setOnClickListener(this)
            this.onCryptoListener = onCryptoListener
        }
        override fun onClick(v: View?)
        {
            onCryptoListener.onCurrencyClick(adapterPosition)
        }

        interface OnCryptoListener
        {
            fun onCurrencyClick(position: Int)
        }

    }

}