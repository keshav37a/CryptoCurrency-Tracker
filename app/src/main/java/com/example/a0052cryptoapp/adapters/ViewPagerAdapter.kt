package com.example.a0052cryptoapp.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.example.a0052cryptoapp.fragments.GraphFragment
import com.example.a0052cryptoapp.models.InfoData

class ViewPagerAdapter(fm: FragmentManager?,
                       alFragments:ArrayList<GraphFragment>,
                       alCryptoDetails:ArrayList<InfoData>,
                       position:Int) : FragmentPagerAdapter(fm)
{
    var alFragments = alFragments
    var alCryptoInfoDetails = alCryptoDetails
    var position2 = position

    override fun getItem(position: Int): GraphFragment
    {
//        Log.d("viewPagerAdapter:", "GetItem : position = $position")
       return when (position)
       {
            0 -> GraphFragment.newInstance("1H", alCryptoInfoDetails, position2)
            1 -> GraphFragment.newInstance("1D", alCryptoInfoDetails, position2)
            2 -> GraphFragment.newInstance("7D", alCryptoInfoDetails, position2)
            3 -> GraphFragment.newInstance("30D", alCryptoInfoDetails, position2)
            4 -> GraphFragment.newInstance("6M", alCryptoInfoDetails, position2)
            5 -> GraphFragment.newInstance("1Y", alCryptoInfoDetails, position2)
            else -> GraphFragment.newInstance("1", alCryptoInfoDetails, position2)
        }
    }

    override fun getCount(): Int {
        return alFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence?
    {
//        Log.d("viewPagerAdapter:", "GetPageTitle : position = $position")
        return when(position)
        {
            0->"1H"
            1->"1D"
            2->"7D"
            3->"30D"
            4->"6M"
            5->"1Y"
            else->"invalid"
        }
    }
}