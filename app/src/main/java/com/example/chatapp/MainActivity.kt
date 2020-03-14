package com.example.chatapp

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.chatapp.ui.main.Adaptor.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.Gravity.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.example.chatapp.ui.main.Fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.popup_fab.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    lateinit var viewPager:ViewPager
    lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,supportFragmentManager

            )
        viewPager = findViewById(R.id.view_pager)
        viewPager.setPageTransformer(true, PageTransformer())
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            val inflater:LayoutInflater=getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view=inflater.inflate(R.layout.popup_fab,null,false)
            val popup=PopupWindow(view,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                popup.elevation=10.0F
            }
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                var slide= Slide()
                slide.slideEdge= TOP
                popup.enterTransition=slide

                val slideOut=Slide()
                slideOut.slideEdge= END
                popup.exitTransition=slideOut
            }
            TransitionManager.beginDelayedTransition(popup_parent)
            popup.showAtLocation(popup_parent, CENTER,0,0)
            Handler().postDelayed({
                popup.dismiss()
                startActivity(Intent(this,NumbersActivity::class.java))
            },500)

        }
    }
}

private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f

class PageTransformer : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}
