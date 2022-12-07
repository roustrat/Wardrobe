package com.str.wardrobe.simpleMVVM

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.str.foundation.ARG_SCREEN
import com.str.foundation.ActivityScopeViewModel
import com.str.foundation.navigator.IntermediateNavigator
import com.str.foundation.navigator.StackFragmentNavigator
import com.str.foundation.uiactions.AndroidUiActions
import com.str.foundation.utils.viewModelCreator
import com.str.foundation.views.FragmentsHolder
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment
import com.str.wardrobe.simpleMVVM.views.categoryinfo.CategoryInfoFragment


class MainActivity : AppCompatActivity(), FragmentsHolder {

    private lateinit var navigator: StackFragmentNavigator

    // Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            uiActions = AndroidUiActions(applicationContext),
            navigator = IntermediateNavigator()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragmentContainer,
            initialScreenCreator = {
                DressesCategoryFragment.Screen()
            }
        )
        navigator.onCreate(savedInstanceState)

        // Call findViewById on the DrawerLayout
        drawerLayout = findViewById(R.id.drawer_container)
        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navView)
        // Call findViewById on the Toolbar
        toolbar = findViewById(R.id.toolbar)

        // Set a Toolbar
        setSupportActionBar(toolbar)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Pass the ActionBarToggle action into the drawerListener
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_open
        )
        drawerLayout.addDrawerListener(toggle)


        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
//        actionBarToggle.isDrawerIndicatorEnabled = true
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

//        // This will display an Up icon (<-), we will replace it with hamburger later
////        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeButtonEnabled(true)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_new_category -> {
                    this.drawerLayout.closeDrawer(GravityCompat.START)
                    val screen = CategoryInfoFragment.Screen()
                    // as screen classes are inside fragments -> we can create fragment directly from screen
                    val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
                    // set screen object as fragment's argument
                    fragment.arguments = bundleOf(ARG_SCREEN to screen)

                    val transaction = this.supportFragmentManager.beginTransaction()
                    transaction
                        .setCustomAnimations(
                            R.anim.enter,
                            R.anim.exit,
                            R.anim.pop_enter,
                            R.anim.pop_exit
                        )
                        .replace(R.id.fragmentContainer, fragment)
                        .commit()
                    Toast.makeText(this, "New category", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_list_of_categories -> {
                    Toast.makeText(this, "List of categories", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_about -> {
                    Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }

        val backPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() = drawerLayout.closeDrawers()
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                backPressedCallback.isEnabled = true
            }

            override fun onDrawerClosed(drawerView: View) {
                backPressedCallback.isEnabled = false
            }

            override fun onDrawerStateChanged(newState: Int) {
                when(newState) {
                    DrawerLayout.STATE_IDLE -> {

                    }
                    DrawerLayout.STATE_DRAGGING -> {

                    }
                    DrawerLayout.STATE_SETTLING -> {

                    }
                    else -> {

                    }
                }
            }

        })

        onBackPressedDispatcher.addCallback(this, backPressedCallback)


    }

    override fun onDestroy() {
        navigator.onDestroy()
        super.onDestroy()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        drawerLayout.openDrawer(navView)
//        onBackPressed()
//        return true
//    }

    // override the onBackPressed() function to close the Drawer when the back button is clicked
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            this.drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        activityViewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        super.onPause()
        // postpone navigation actions if activity is not active
        activityViewModel.navigator.setTarget(null)
    }

    override fun notifyScreenUpdates() {
        notifyScreenUpdates()
    }

    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }
}