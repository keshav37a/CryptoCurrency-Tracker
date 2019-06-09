package com.example.a0052cryptoapp

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.a0052cryptoapp.adapters.InfoAdapter
import com.example.a0052cryptoapp.database.CryptoDatabase
import com.example.a0052cryptoapp.fragments.ConverterFragment
import com.example.a0052cryptoapp.fragments.InfoFragment
import com.example.a0052cryptoapp.fragments.NewsFragment
import com.example.a0052cryptoapp.models.InfoData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.info_fragment.*
import java.io.Serializable
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    //Load data from database instead of running after intents all the time
    private val dbCrypto by lazy {
        Room.databaseBuilder(
            this,
            CryptoDatabase::class.java,
            "newsDb.dbHeadlines"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    var flag = false
    var sortFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        displayFragment(-1)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val alInfoDataMain = dbCrypto.cryptoDao().loadAllCurrencies() as ArrayList<InfoData>
        return when (item.itemId)
        {
            R.id.sort ->
            {
                if(flag==false)
                {
                    flag = true
                    toolbarSortOptions.visibility = View.VISIBLE
                    toolbarSortOptions.inflateMenu(R.menu.sort_menu)
                    toolbarSortOptions.setOnMenuItemClickListener{
                       when(it.itemId)
                       {
                           R.id.SortByName-> {
                                Toast.makeText(this, "SortByName Selected", Toast.LENGTH_SHORT).show()
                                when(sortFlag)
                                {
                                    false->{
                                        Collections.sort(alInfoDataMain) { o1, o2 -> o1?.CoinInfo?.Name!!.compareTo(o2?.CoinInfo?.Name!!)}
                                        sortFlag = true
                                    }
                                    else->{
                                        Collections.sort(alInfoDataMain) { o2, o1 -> o1?.CoinInfo?.Name!!.compareTo(o2?.CoinInfo?.Name!!)}
                                        sortFlag = false
                                    }
                                }
                                Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                                setAdapter(alInfoDataMain)
                                true
                            }
                           R.id.SortByPrice-> {
                               Toast.makeText(this, "SortByPrice Selected", Toast.LENGTH_SHORT).show()
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               when(sortFlag)
                               {
                                   false->{
                                       Collections.sort(alInfoDataMain) { o1, o2 -> o1?.RAW?.USD?.PRICE!!.compareTo(o2?.RAW?.USD?.PRICE!!)}
                                       sortFlag = true
                                   }
                                   else->{
                                       Collections.sort(alInfoDataMain) { o2, o1 -> o1?.RAW?.USD?.PRICE!!.compareTo(o2?.RAW?.USD?.PRICE!!)}
                                       sortFlag = false
                                   }
                               }
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               setAdapter(alInfoDataMain)
                               true
                           }
                           R.id.SortByMarketCap-> {
                               Toast.makeText(this, "SortByMarketCap Selected", Toast.LENGTH_SHORT).show()
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               when(sortFlag)
                               {
                                   false->{
                                       Collections.sort(alInfoDataMain) { o1, o2 -> o1?.RAW?.USD?.MKTCAP!!.compareTo(o2?.RAW?.USD?.MKTCAP!!)}
                                       sortFlag = true
                                   }
                                   else->{
                                       Collections.sort(alInfoDataMain) { o2, o1 -> o1?.RAW?.USD?.MKTCAP!!.compareTo(o2?.RAW?.USD?.MKTCAP!!)}
                                       sortFlag = false
                                   }
                               }
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               setAdapter(alInfoDataMain)
                               true
                           }
                           R.id.SortByVolume-> {
                               Toast.makeText(this, "SortByVolume Selected", Toast.LENGTH_SHORT).show()
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               when(sortFlag)
                               {
                                   false->{
                                       Collections.sort(alInfoDataMain) { o1, o2 -> o1?.RAW?.USD?.TOTALVOLUME24HTO!!.compareTo(o2?.RAW?.USD?.VOLUME24HOURTO!!)}
                                       sortFlag = true
                                   }
                                   else->{
                                       Collections.sort(alInfoDataMain) { o2, o1 -> o1?.RAW?.USD?.TOTALVOLUME24HTO!!.compareTo(o2?.RAW?.USD?.VOLUME24HOURTO!!)}
                                       sortFlag = false
                                   }
                               }
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               setAdapter(alInfoDataMain)
                               true
                           }
                           R.id.SortByPercentage1H-> {
                               Toast.makeText(this, "SortByPercentage1H Selected", Toast.LENGTH_SHORT).show()
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               when(sortFlag)
                               {
                                   false->{
                                       Collections.sort(alInfoDataMain) { o1, o2 -> o1?.RAW?.USD?.CHANGEPCT24HOUR!!.compareTo(o2?.RAW?.USD?.CHANGEPCT24HOUR!!)}
                                       sortFlag = true
                                   }
                                   else->{
                                       Collections.sort(alInfoDataMain) { o2, o1 -> o1?.RAW?.USD?.CHANGEPCT24HOUR!!.compareTo(o2?.RAW?.USD?.CHANGEPCT24HOUR!!)}
                                       sortFlag = false
                                   }
                               }
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               setAdapter(alInfoDataMain)
                               true
                           }
                           R.id.SortByPercentage24H-> {
                               Toast.makeText(this, "SortByPercentage24H", Toast.LENGTH_SHORT).show()
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               when(sortFlag)
                               {
                                   false->{
                                       Collections.sort(alInfoDataMain) { o1, o2 -> o1?.RAW?.USD?.CHANGEPCTDAY!!.compareTo(o2?.RAW?.USD?.CHANGEPCTDAY!!)}
                                       sortFlag = true
                                   }
                                   else->{
                                       Collections.sort(alInfoDataMain) { o2, o1 -> o1?.RAW?.USD?.CHANGEPCTDAY!!.compareTo(o2?.RAW?.USD?.CHANGEPCTDAY!!)}
                                       sortFlag = false
                                   }
                               }
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               setAdapter(alInfoDataMain)
                               true
                           }
                           R.id.SortBySupply-> {
                               Toast.makeText(this, "SortBySupply Selected", Toast.LENGTH_SHORT).show()
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               when(sortFlag)
                               {
                                   false->{
                                       Collections.sort(alInfoDataMain) { o1, o2 -> o1?.RAW?.USD?.SUPPLY!!.compareTo(o2?.RAW?.USD?.SUPPLY!!)}
                                       sortFlag = true
                                   }
                                   else->{
                                       Collections.sort(alInfoDataMain) { o2, o1 -> o1?.RAW?.USD?.SUPPLY!!.compareTo(o2?.RAW?.USD?.SUPPLY!!)}
                                       sortFlag = false
                                   }
                               }
                               Log.d("MainActivity:", "before Sorting $alInfoDataMain")
                               setAdapter(alInfoDataMain)
                               true
                           }
                           else->true
                        }
                    }
                    Log.d("MainActivity", "flag = $flag")
                }
                else
                {
                    flag = false
                    toolbarSortOptions.visibility = View.GONE
                    toolbarSortOptions.menu.clear()
                    Log.d("MainActivity", "flag = $flag")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayFragment(itemId:Int)
    {
        var fragment:Fragment
        when(itemId)
        {
            R.id.home->{
                fragment = InfoFragment()
                Log.d("TAG", "HOME SELECTED")
            }

            R.id.news->{
                fragment = NewsFragment()
                Log.d("TAG", "News SELECTED")
            }
            R.id.converter->{
                fragment = ConverterFragment()
                Log.d("TAG", "Converter SELECTED")
            }
            else->{
                fragment = InfoFragment()
                Log.d("TAG", "Else SELECTED")
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_content_main, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        drawer_layout.closeDrawer(GravityCompat.START)
        displayFragment(item.itemId)
        return true
    }
    private fun setAdapter(alInfoDataMain:ArrayList<InfoData>)
    {
        val infoAdapter = InfoAdapter(alInfoDataMain, object:InfoAdapter.InfoViewHolder.OnCryptoListener{
            override fun onCurrencyClick(position: Int)
            {
                Log.d("Log", "onCurrencyClicked at position $position")
                //sending data to cryptoDetails Activity and starting activity
                val intent = Intent(this@MainActivity, CryptoDetails::class.java)
                intent.putExtra("position", position)
                val bundle = Bundle()
                bundle.putSerializable("ArrayList", alInfoDataMain as Serializable)
                intent.putExtra("Bundle", bundle)
                Log.d("onCurrencyClick ", "alCryptoInfo size = ${alInfoDataMain.size}")
                Log.d("onCurrencyClicked:", " $position")
                startActivity(intent)
            }
        })
        recycler_view_info_fragment.adapter = infoAdapter
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        Log.d("onAttach ", "called")
        if(toolbarSortOptions!=null)
            toolbarSortOptions.visibility = View.GONE
    }
}
