package com.example.a0052cryptoapp.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a0052cryptoapp.R
import com.example.a0052cryptoapp.models.NewsData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_info_item_row.view.*
import java.util.*

class NewsAdapter(private var alCryptoNews:ArrayList<NewsData>,
                  private var onNewsListener: NewsHolder.OnNewsListener):
    RecyclerView.Adapter<NewsAdapter.NewsHolder>()
{
    private var day = "Huh"
    var hour = "Huh"
    var minutes = "Huh"
    var amPm = "huh"
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): NewsHolder
    {
        val inflatedView = LayoutInflater.from(parent.context).
            inflate(R.layout.news_info_item_row, parent, false)
        return NewsHolder(inflatedView, onNewsListener)
    }

    override fun getItemCount(): Int {
        return alCryptoNews.size
    }

    override fun onBindViewHolder(parent: NewsHolder, position: Int)
    {
        Picasso.get().load(alCryptoNews[position].imageurl)
            .resize(1000, 600)
            .centerCrop()
            .into(parent.itemView.imgViewCryptoNews)

        Picasso.get().load(alCryptoNews[position].source_info.img)
            .resize(40, 40)
            .centerCrop()
            .into(parent.itemView.imgViewSmallCryptoNews)
        Log.d("NewsImage", "${alCryptoNews[position].source_info.img}")
        Log.d("NewsTitle", "${alCryptoNews[position].title}")
        parent.itemView.tvCryptoTitle.text = alCryptoNews[position].title
        parent.itemView.tvCryptoSource.text = alCryptoNews[position].source
        parent.itemView.tvCryptoPublishedOn.text = alCryptoNews[position].published_on.toString()

        dateParsing(alCryptoNews, position)
        parent.itemView.tvCryptoPublishedOn.text = "$day $hour:$minutes $amPm"
    }

    class NewsHolder(itemView: View,
                     onNewsListener: OnNewsListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        var onNewsListener = onNewsListener
        init {
            itemView.setOnClickListener(this)
            this.onNewsListener = onNewsListener
        }
        override fun onClick(v: View?)
        {
            onNewsListener.onNewsClick(adapterPosition)
        }
        interface OnNewsListener
        {
            fun onNewsClick(position: Int)
        }
    }

    private fun dateParsing(alCryptoNews: ArrayList<NewsData>, position: Int)
    {
        val dayName = arrayOf("Sunday","Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday")
        val amPmValue = arrayOf("am", "pm")
        val valueLong:Long = alCryptoNews[position].published_on
        val timeInMilli: Date = java.util.Date((valueLong*1000))
        val calendar = Calendar.getInstance()
        calendar.time = timeInMilli
        day = dayName[calendar.get(Calendar.DAY_OF_WEEK)-1]
        Log.d("Day:", "$day")
        hour = calendar.get(Calendar.HOUR_OF_DAY).toString()
        minutes = calendar.get(Calendar.MINUTE).toString()
        if(minutes.toInt()<10)
            minutes = "0$minutes"
        amPm = amPmValue[calendar.get(Calendar.AM_PM)]
    }
}



