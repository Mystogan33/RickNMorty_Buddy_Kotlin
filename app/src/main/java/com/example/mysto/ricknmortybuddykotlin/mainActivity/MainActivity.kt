package com.example.mysto.ricknmortybuddykotlin.mainActivity

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.mysto.ricknmortybuddykotlin.Fragments.Characters.Characters_Fragment
import com.example.mysto.ricknmortybuddykotlin.Fragments.Episodes.Episodes_Fragment
import com.example.mysto.ricknmortybuddykotlin.Fragments.Locations.Locations_Fragment
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.animations.DepthPageTransformer
import com.example.mysto.ricknmortybuddykotlin.mainActivity.adapter.ViewPagerAdapter
import com.example.mysto.ricknmortybuddykotlin.notifications.NotificationHelperWelcomeBack
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    @BindView(R.id.tablayout_id)
    lateinit var mTabLayout: TabLayout
    @BindView(R.id.viewpager_id)
    lateinit var mViewPager: ViewPager
    @BindView(R.id.drawer_layout)
    lateinit var mDrawerLayout: DrawerLayout
    @BindView(R.id.nav_view)
    lateinit var navigationView: NavigationView
    @BindView(R.id.menu_button)
    lateinit var home_button: ImageButton
    @BindView(R.id.more_button)
    lateinit var more_button: ImageButton
    @BindView(R.id.refresh_button)
    lateinit var refresh_button: FloatingActionButton

    var mNotifier: NotificationHelperWelcomeBack? = null
    var mViewPagerAdapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        navigationView.setNavigationItemSelectedListener{
            it.isChecked = true
            mDrawerLayout.closeDrawers()
            true
        }

        this.initFragments()
        this.sendNotifications()
        //this.addTransitionAnimations()

    }

    @OnClick(R.id.menu_button)
    fun openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START)
    }

    @OnClick(R.id.more_button)
    fun openOptions() {
        initSequence()
    }

    @OnClick(R.id.refresh_button)
    fun refreshAllFragments() {
        mViewPagerAdapter!!.refreshAllFragments()
    }

    fun addTransitionAnimations() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mViewPager.setPageTransformer(true, DepthPageTransformer())
            //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }
    }

    fun initFragments() {
        mViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        mViewPagerAdapter!!.addFragment(Characters_Fragment() as Fragment, resources.getString(R.string.tab_title_characters))
        mViewPagerAdapter!!.addFragment(Episodes_Fragment() as Fragment, resources.getString(R.string.tab_title_episodes))
        mViewPagerAdapter!!.addFragment(Locations_Fragment() as Fragment, resources.getString(R.string.tab_title_locations))

        mViewPager.adapter = mViewPagerAdapter
        mViewPager.offscreenPageLimit = 2
        mTabLayout.setupWithViewPager(mViewPager)
    }

    fun sendNotifications() {
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

    fun initSequence() {
        val sequence = TapTargetSequence(this)
            .targets(
                TapTarget.forView(home_button, "Menu", "Vous pouvez ouvrir le menu ici")
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
                TapTarget.forView(more_button, "Plus d'options", "Plus d'options ici ! :)")
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
                    refresh_button,
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
                            handler.postDelayed({ mDrawerLayout.closeDrawers() }, 500)
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
