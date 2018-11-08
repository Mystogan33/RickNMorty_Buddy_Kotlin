package com.example.mysto.ricknmortybuddykotlin.mainActivity

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.CharactersFragment
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.EpisodesFragment
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.LocationsFragment
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.animations.DepthPageTransformer
import com.example.mysto.ricknmortybuddykotlin.mainActivity.adapter.ViewPagerAdapter
import com.example.mysto.ricknmortybuddykotlin.notifications.NotificationHelperWelcomeBack
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import kotlinx.android.synthetic.main.include_main_activity.*
import kotlinx.android.synthetic.main.main_activity.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var mNotifier: NotificationHelperWelcomeBack? = null
    var mViewPagerAdapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        mainActivityNavigationView.setNavigationItemSelectedListener{
            it.isChecked = true
            mainActivityDrawerLayout.closeDrawers()
            true
        }

        this.initFragments()
        this.sendNotifications()
        this.setListeners()
        //this.addTransitionAnimations()
    }

    private fun setListeners() {
        mainActivityMenuButton.setOnClickListener {
            openDrawer()
        }
        mainActivityMoreButton.setOnClickListener {
            openOptions()
        }
        mainActivityRefreshButton.setOnClickListener {
            refreshAllFragments()
        }
    }

    fun openDrawer() {
        mainActivityDrawerLayout.openDrawer(GravityCompat.START)
    }

    fun openOptions() {
        initSequence()
    }

    fun refreshAllFragments() {
        mViewPagerAdapter!!.refreshAllFragments()
    }

    fun addTransitionAnimations() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mainActivityViewPager.setPageTransformer(true, DepthPageTransformer())
            //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }
    }

    private fun initFragments() {
        mViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        mViewPagerAdapter!!.addFragment(CharactersFragment() as Fragment, resources.getString(R.string.tab_title_characters))
        mViewPagerAdapter!!.addFragment(EpisodesFragment() as Fragment, resources.getString(R.string.tab_title_episodes))
        mViewPagerAdapter!!.addFragment(LocationsFragment() as Fragment, resources.getString(R.string.tab_title_locations))

        mainActivityViewPager.adapter = mViewPagerAdapter
        mainActivityViewPager.offscreenPageLimit = 2
        mainActivityTabLayout.setupWithViewPager(mainActivityViewPager)
    }

    private fun sendNotifications() {
        val cal = Calendar.getInstance()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).format(cal.time)
        val sharedPreferences = this.getSharedPreferences("APP_DATA", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("last_launch_time", null)

        if (json == null) {
            sharedPreferences.edit()
                .putString("last_launch_time", date)
                .apply()
            initSequence()
        } else {
            if (json != date) {
                mNotifier = NotificationHelperWelcomeBack(this)
                mNotifier!!.createNotification(
                    "Welcome Back",
                    "Thanks to visit Rick & Morty ! Hope you get swifty. Stay connected !"
                )
                sharedPreferences.edit()
                    .putString("last_launch_time", date)
                    .apply()
            }
        }
    }

    private fun initSequence() {
        val sequence = TapTargetSequence(this)
            .targets(
                TapTarget.forView(mainActivityMenuButton, "Menu", "Vous pouvez ouvrir le menu ici")
                    .outerCircleColor(R.color.tabindicatorcolor)      // Specify a color for the outer circle
                    .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                    .targetCircleColor(R.color.colorAccent)   // Specify a color for the target circle
                    .titleTextSize(20)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                    .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                    .descriptionTextColor(R.color.colorAccent)  // Specify the color of the description text
                    .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                    .dimColor(R.color.followersBg)            // If set, will dim behind the view with 30% opacity of the given color
                    .drawShadow(true)                   // Whether to draw a drop shadow or not
                    .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                    .tintTarget(true)                   // Whether to tint the target view's color
                    .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                    .targetRadius(60)
                    .id(1),
                TapTarget.forView(mainActivityMoreButton, "Plus d'options", "Plus d'options ici ! :)")
                    .outerCircleColor(R.color.tabindicatorcolor)      // Specify a color for the outer circle
                    .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                    .targetCircleColor(R.color.colorAccent)   // Specify a color for the target circle
                    .titleTextSize(20)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                    .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                    .descriptionTextColor(R.color.colorAccent)  // Specify the color of the description text
                    .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                    .dimColor(R.color.followersBg)            // If set, will dim behind the view with 30% opacity of the given color
                    .drawShadow(true)                   // Whether to draw a drop shadow or not
                    .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                    .tintTarget(true)                   // Whether to tint the target view's color
                    .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                    .targetRadius(60)
                    .id(2),
                TapTarget.forView(
                    mainActivityRefreshButton,
                    "Rafraichir toutes les catégories",
                    "(A noter: Toutes les catégories sont automatiquement rafraichies chaque jour et peuvent être rafraichis avec ce bouton mais vous pouvez les rafraichir manuellement et séparément en swipant vers le bas en haut de chacune des catégories)"
                )
                    .outerCircleColor(R.color.tabindicatorcolor)      // Specify a color for the outer circle
                    .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                    .titleTextSize(20)                  // Specify the size (in sp) of the title text
                    .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                    .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                    .descriptionTextColor(R.color.colorAccent)  // Specify the color of the description text
                    .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                    .drawShadow(true)                   // Whether to draw a drop shadow or not
                    .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                    .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                    .targetRadius(60)
                    .id(3)
            )
            .listener(object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    Toast.makeText(applicationContext, "You know everything now Morty !", Toast.LENGTH_SHORT).show()
                }
                override fun onSequenceStep(lastTarget: TapTarget, bool: Boolean) {
                    when (lastTarget.id()) {
                        1 -> {
                            openDrawer()
                            val handler = Handler()
                            handler.postDelayed({ mainActivityDrawerLayout.closeDrawers() }, 500)
                        }
                        3 -> refreshAllFragments()
                    }
                }
                override fun onSequenceCanceled(lastTarget: TapTarget) {
                    Toast.makeText(
                        applicationContext,
                        "Why you left me Morty ! You can't left me !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        sequence.start()
    }
}
