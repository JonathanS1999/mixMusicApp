package com.example.musicx.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.musicx.AlbumFragment
import com.example.musicx.FragmentModeConductor.FragmentModeConductor
import com.example.musicx.Fragment_Player_card
import com.example.musicx.Fragments.ViewPagerAdapter
import com.example.musicx.R
import com.example.musicx.SongsFragment

class CustomList ( ) : Fragment() {

    private lateinit var viewPager2 : ViewPager2
    private lateinit var linearPuntos :LinearLayout
    private  var themeSelected : OnThemeSelected? = null
    private var customList:OnCustomList?=null
    private var type=1
    private var themePlay_or_Controls=1
    private val range=12

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnThemeSelected) themeSelected = context
        if(context is OnCustomList) customList = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return  inflater.inflate(R.layout.fragment_custom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            themePlay_or_Controls = bundle.getInt("config_controls_or_play",1)
        }
        viewPager2 = view.findViewById(R.id.viewpager2)
        linearPuntos = view.findViewById(R.id.linearPuntos)
        initViewPager()
    }


    private fun initViewPager() {
            val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
            if(customList != null) type = customList!!.getThemeConfig()// interface
            if(type == 1){
                viewPagerAdapter.addFragments(FragmentListNormal(), "fragA")
                viewPagerAdapter.addFragments(SongsFragment(),"fragB")
                for(i in 3..5 ){
                    val aF1=AlbumFragment()
                    val bundle=Bundle()
                    bundle.putInt("idStyle",i)
                    aF1.arguments=bundle
                    viewPagerAdapter.addFragments(aF1, "frag$i")
                }
            }
            else if(type == 2){
                 customList!!.startServiceMusic()
                for (i in 1..range){
                    val fragmet = FragmentCardImg()
                    val bundle1 =  Bundle()
                    bundle1.putInt("idStyleCard",i)
                    bundle1.putInt("config_controls_or_play",themePlay_or_Controls)
                    fragmet.arguments=bundle1
                    viewPagerAdapter.addFragments(fragmet,"fragCard$i")
                }
            }else if(type == 3){
                for (i in 1..range){
                    val fragmet = FragmentModeConductor()
                    val bundle1 =  Bundle()
                    bundle1.putInt("idStyleMC",i)
                    bundle1.putInt("config_MC",themePlay_or_Controls)
                    fragmet.arguments=bundle1
                    viewPagerAdapter.addFragments(fragmet,"fragMC$i")
                }
            }

            viewPager2.setAdapter(viewPagerAdapter)
            viewPager2.registerOnPageChangeCallback(viewListener2)
    }

    private fun agregarIndicadorPuntos(pos: Int) {
        val puntosSlide = arrayOfNulls<TextView>(range)
        linearPuntos.removeAllViews()
        for (i in puntosSlide.indices) {
            puntosSlide[i] = TextView(context)
            val params = LinearLayout.LayoutParams(20, 20)
            params.setMargins(2, 6, 2, 6)
            puntosSlide[i]!!.layoutParams = params
            puntosSlide[i]!!.background = context?.let { ContextCompat.getDrawable(it, R.drawable.incators_points_a) }
            linearPuntos.addView(puntosSlide[i])
        }
        puntosSlide[pos]!!.background = context?.let { ContextCompat.getDrawable(it, R.drawable.indicators_points_b) }
        themeSelected?.setMyTheme(pos)
    }

    var viewListener2: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            agregarIndicadorPuntos(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    interface OnCustomList{
        fun getThemeConfig():Int
        fun startServiceMusic()
    }
}